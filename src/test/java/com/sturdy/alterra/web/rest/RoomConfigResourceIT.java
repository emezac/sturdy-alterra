package com.sturdy.alterra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sturdy.alterra.IntegrationTest;
import com.sturdy.alterra.domain.RoomConfig;
import com.sturdy.alterra.repository.RoomConfigRepository;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoomConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomConfigResourceIT {

    private static final String DEFAULT_SETUP = "AAAAAAAAAA";
    private static final String UPDATED_SETUP = "BBBBBBBBBB";

    private static final Long DEFAULT_NUM_OF_DOORS = 1L;
    private static final Long UPDATED_NUM_OF_DOORS = 2L;

    private static final Long DEFAULT_NUM_OF_PRIZES = 1L;
    private static final Long UPDATED_NUM_OF_PRIZES = 2L;

    private static final String ENTITY_API_URL = "/api/room-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RoomConfigRepository roomConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomConfigMockMvc;

    private RoomConfig roomConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomConfig createEntity(EntityManager em) {
        RoomConfig roomConfig = new RoomConfig().setup(DEFAULT_SETUP).numOfDoors(DEFAULT_NUM_OF_DOORS).numOfPrizes(DEFAULT_NUM_OF_PRIZES);
        return roomConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomConfig createUpdatedEntity(EntityManager em) {
        RoomConfig roomConfig = new RoomConfig().setup(UPDATED_SETUP).numOfDoors(UPDATED_NUM_OF_DOORS).numOfPrizes(UPDATED_NUM_OF_PRIZES);
        return roomConfig;
    }

    @BeforeEach
    public void initTest() {
        roomConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomConfig() throws Exception {
        int databaseSizeBeforeCreate = roomConfigRepository.findAll().size();
        // Create the RoomConfig
        restRoomConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomConfig)))
            .andExpect(status().isCreated());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeCreate + 1);
        RoomConfig testRoomConfig = roomConfigList.get(roomConfigList.size() - 1);
        assertThat(testRoomConfig.getSetup()).isEqualTo(DEFAULT_SETUP);
        assertThat(testRoomConfig.getNumOfDoors()).isEqualTo(DEFAULT_NUM_OF_DOORS);
        assertThat(testRoomConfig.getNumOfPrizes()).isEqualTo(DEFAULT_NUM_OF_PRIZES);
    }

    @Test
    @Transactional
    void createRoomConfigWithExistingId() throws Exception {
        // Create the RoomConfig with an existing ID
        roomConfigRepository.saveAndFlush(roomConfig);

        int databaseSizeBeforeCreate = roomConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomConfig)))
            .andExpect(status().isBadRequest());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoomConfigs() throws Exception {
        // Initialize the database
        roomConfigRepository.saveAndFlush(roomConfig);

        // Get all the roomConfigList
        restRoomConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomConfig.getId().toString())))
            .andExpect(jsonPath("$.[*].setup").value(hasItem(DEFAULT_SETUP)))
            .andExpect(jsonPath("$.[*].numOfDoors").value(hasItem(DEFAULT_NUM_OF_DOORS.intValue())))
            .andExpect(jsonPath("$.[*].numOfPrizes").value(hasItem(DEFAULT_NUM_OF_PRIZES.intValue())));
    }

    @Test
    @Transactional
    void getRoomConfig() throws Exception {
        // Initialize the database
        roomConfigRepository.saveAndFlush(roomConfig);

        // Get the roomConfig
        restRoomConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, roomConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomConfig.getId().toString()))
            .andExpect(jsonPath("$.setup").value(DEFAULT_SETUP))
            .andExpect(jsonPath("$.numOfDoors").value(DEFAULT_NUM_OF_DOORS.intValue()))
            .andExpect(jsonPath("$.numOfPrizes").value(DEFAULT_NUM_OF_PRIZES.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRoomConfig() throws Exception {
        // Get the roomConfig
        restRoomConfigMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomConfig() throws Exception {
        // Initialize the database
        roomConfigRepository.saveAndFlush(roomConfig);

        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();

        // Update the roomConfig
        RoomConfig updatedRoomConfig = roomConfigRepository.findById(roomConfig.getId()).get();
        // Disconnect from session so that the updates on updatedRoomConfig are not directly saved in db
        em.detach(updatedRoomConfig);
        updatedRoomConfig.setup(UPDATED_SETUP).numOfDoors(UPDATED_NUM_OF_DOORS).numOfPrizes(UPDATED_NUM_OF_PRIZES);

        restRoomConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoomConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoomConfig))
            )
            .andExpect(status().isOk());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
        RoomConfig testRoomConfig = roomConfigList.get(roomConfigList.size() - 1);
        assertThat(testRoomConfig.getSetup()).isEqualTo(UPDATED_SETUP);
        assertThat(testRoomConfig.getNumOfDoors()).isEqualTo(UPDATED_NUM_OF_DOORS);
        assertThat(testRoomConfig.getNumOfPrizes()).isEqualTo(UPDATED_NUM_OF_PRIZES);
    }

    @Test
    @Transactional
    void putNonExistingRoomConfig() throws Exception {
        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();
        roomConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomConfig() throws Exception {
        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();
        roomConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomConfig() throws Exception {
        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();
        roomConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomConfigWithPatch() throws Exception {
        // Initialize the database
        roomConfigRepository.saveAndFlush(roomConfig);

        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();

        // Update the roomConfig using partial update
        RoomConfig partialUpdatedRoomConfig = new RoomConfig();
        partialUpdatedRoomConfig.setId(roomConfig.getId());

        partialUpdatedRoomConfig.numOfDoors(UPDATED_NUM_OF_DOORS).numOfPrizes(UPDATED_NUM_OF_PRIZES);

        restRoomConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomConfig))
            )
            .andExpect(status().isOk());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
        RoomConfig testRoomConfig = roomConfigList.get(roomConfigList.size() - 1);
        assertThat(testRoomConfig.getSetup()).isEqualTo(DEFAULT_SETUP);
        assertThat(testRoomConfig.getNumOfDoors()).isEqualTo(UPDATED_NUM_OF_DOORS);
        assertThat(testRoomConfig.getNumOfPrizes()).isEqualTo(UPDATED_NUM_OF_PRIZES);
    }

    @Test
    @Transactional
    void fullUpdateRoomConfigWithPatch() throws Exception {
        // Initialize the database
        roomConfigRepository.saveAndFlush(roomConfig);

        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();

        // Update the roomConfig using partial update
        RoomConfig partialUpdatedRoomConfig = new RoomConfig();
        partialUpdatedRoomConfig.setId(roomConfig.getId());

        partialUpdatedRoomConfig.setup(UPDATED_SETUP).numOfDoors(UPDATED_NUM_OF_DOORS).numOfPrizes(UPDATED_NUM_OF_PRIZES);

        restRoomConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomConfig))
            )
            .andExpect(status().isOk());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
        RoomConfig testRoomConfig = roomConfigList.get(roomConfigList.size() - 1);
        assertThat(testRoomConfig.getSetup()).isEqualTo(UPDATED_SETUP);
        assertThat(testRoomConfig.getNumOfDoors()).isEqualTo(UPDATED_NUM_OF_DOORS);
        assertThat(testRoomConfig.getNumOfPrizes()).isEqualTo(UPDATED_NUM_OF_PRIZES);
    }

    @Test
    @Transactional
    void patchNonExistingRoomConfig() throws Exception {
        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();
        roomConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomConfig() throws Exception {
        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();
        roomConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomConfig() throws Exception {
        int databaseSizeBeforeUpdate = roomConfigRepository.findAll().size();
        roomConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomConfig in the database
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomConfig() throws Exception {
        // Initialize the database
        roomConfigRepository.saveAndFlush(roomConfig);

        int databaseSizeBeforeDelete = roomConfigRepository.findAll().size();

        // Delete the roomConfig
        restRoomConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomConfig.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomConfig> roomConfigList = roomConfigRepository.findAll();
        assertThat(roomConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
