package com.sturdy.alterra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sturdy.alterra.IntegrationTest;
import com.sturdy.alterra.domain.FloorConfig;
import com.sturdy.alterra.repository.FloorConfigRepository;
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
 * Integration tests for the {@link FloorConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FloorConfigResourceIT {

    private static final String DEFAULT_SETUP = "AAAAAAAAAA";
    private static final String UPDATED_SETUP = "BBBBBBBBBB";

    private static final Long DEFAULT_NUM_OF_ROOMS = 1L;
    private static final Long UPDATED_NUM_OF_ROOMS = 2L;

    private static final String ENTITY_API_URL = "/api/floor-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FloorConfigRepository floorConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFloorConfigMockMvc;

    private FloorConfig floorConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloorConfig createEntity(EntityManager em) {
        FloorConfig floorConfig = new FloorConfig().setup(DEFAULT_SETUP).numOfRooms(DEFAULT_NUM_OF_ROOMS);
        return floorConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloorConfig createUpdatedEntity(EntityManager em) {
        FloorConfig floorConfig = new FloorConfig().setup(UPDATED_SETUP).numOfRooms(UPDATED_NUM_OF_ROOMS);
        return floorConfig;
    }

    @BeforeEach
    public void initTest() {
        floorConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createFloorConfig() throws Exception {
        int databaseSizeBeforeCreate = floorConfigRepository.findAll().size();
        // Create the FloorConfig
        restFloorConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorConfig)))
            .andExpect(status().isCreated());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeCreate + 1);
        FloorConfig testFloorConfig = floorConfigList.get(floorConfigList.size() - 1);
        assertThat(testFloorConfig.getSetup()).isEqualTo(DEFAULT_SETUP);
        assertThat(testFloorConfig.getNumOfRooms()).isEqualTo(DEFAULT_NUM_OF_ROOMS);
    }

    @Test
    @Transactional
    void createFloorConfigWithExistingId() throws Exception {
        // Create the FloorConfig with an existing ID
        floorConfigRepository.saveAndFlush(floorConfig);

        int databaseSizeBeforeCreate = floorConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFloorConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorConfig)))
            .andExpect(status().isBadRequest());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSetupIsRequired() throws Exception {
        int databaseSizeBeforeTest = floorConfigRepository.findAll().size();
        // set the field null
        floorConfig.setSetup(null);

        // Create the FloorConfig, which fails.

        restFloorConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorConfig)))
            .andExpect(status().isBadRequest());

        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFloorConfigs() throws Exception {
        // Initialize the database
        floorConfigRepository.saveAndFlush(floorConfig);

        // Get all the floorConfigList
        restFloorConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(floorConfig.getId().toString())))
            .andExpect(jsonPath("$.[*].setup").value(hasItem(DEFAULT_SETUP)))
            .andExpect(jsonPath("$.[*].numOfRooms").value(hasItem(DEFAULT_NUM_OF_ROOMS.intValue())));
    }

    @Test
    @Transactional
    void getFloorConfig() throws Exception {
        // Initialize the database
        floorConfigRepository.saveAndFlush(floorConfig);

        // Get the floorConfig
        restFloorConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, floorConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(floorConfig.getId().toString()))
            .andExpect(jsonPath("$.setup").value(DEFAULT_SETUP))
            .andExpect(jsonPath("$.numOfRooms").value(DEFAULT_NUM_OF_ROOMS.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFloorConfig() throws Exception {
        // Get the floorConfig
        restFloorConfigMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFloorConfig() throws Exception {
        // Initialize the database
        floorConfigRepository.saveAndFlush(floorConfig);

        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();

        // Update the floorConfig
        FloorConfig updatedFloorConfig = floorConfigRepository.findById(floorConfig.getId()).get();
        // Disconnect from session so that the updates on updatedFloorConfig are not directly saved in db
        em.detach(updatedFloorConfig);
        updatedFloorConfig.setup(UPDATED_SETUP).numOfRooms(UPDATED_NUM_OF_ROOMS);

        restFloorConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFloorConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFloorConfig))
            )
            .andExpect(status().isOk());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
        FloorConfig testFloorConfig = floorConfigList.get(floorConfigList.size() - 1);
        assertThat(testFloorConfig.getSetup()).isEqualTo(UPDATED_SETUP);
        assertThat(testFloorConfig.getNumOfRooms()).isEqualTo(UPDATED_NUM_OF_ROOMS);
    }

    @Test
    @Transactional
    void putNonExistingFloorConfig() throws Exception {
        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();
        floorConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, floorConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFloorConfig() throws Exception {
        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();
        floorConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFloorConfig() throws Exception {
        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();
        floorConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFloorConfigWithPatch() throws Exception {
        // Initialize the database
        floorConfigRepository.saveAndFlush(floorConfig);

        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();

        // Update the floorConfig using partial update
        FloorConfig partialUpdatedFloorConfig = new FloorConfig();
        partialUpdatedFloorConfig.setId(floorConfig.getId());

        restFloorConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloorConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloorConfig))
            )
            .andExpect(status().isOk());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
        FloorConfig testFloorConfig = floorConfigList.get(floorConfigList.size() - 1);
        assertThat(testFloorConfig.getSetup()).isEqualTo(DEFAULT_SETUP);
        assertThat(testFloorConfig.getNumOfRooms()).isEqualTo(DEFAULT_NUM_OF_ROOMS);
    }

    @Test
    @Transactional
    void fullUpdateFloorConfigWithPatch() throws Exception {
        // Initialize the database
        floorConfigRepository.saveAndFlush(floorConfig);

        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();

        // Update the floorConfig using partial update
        FloorConfig partialUpdatedFloorConfig = new FloorConfig();
        partialUpdatedFloorConfig.setId(floorConfig.getId());

        partialUpdatedFloorConfig.setup(UPDATED_SETUP).numOfRooms(UPDATED_NUM_OF_ROOMS);

        restFloorConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloorConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloorConfig))
            )
            .andExpect(status().isOk());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
        FloorConfig testFloorConfig = floorConfigList.get(floorConfigList.size() - 1);
        assertThat(testFloorConfig.getSetup()).isEqualTo(UPDATED_SETUP);
        assertThat(testFloorConfig.getNumOfRooms()).isEqualTo(UPDATED_NUM_OF_ROOMS);
    }

    @Test
    @Transactional
    void patchNonExistingFloorConfig() throws Exception {
        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();
        floorConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, floorConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFloorConfig() throws Exception {
        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();
        floorConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFloorConfig() throws Exception {
        int databaseSizeBeforeUpdate = floorConfigRepository.findAll().size();
        floorConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(floorConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FloorConfig in the database
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFloorConfig() throws Exception {
        // Initialize the database
        floorConfigRepository.saveAndFlush(floorConfig);

        int databaseSizeBeforeDelete = floorConfigRepository.findAll().size();

        // Delete the floorConfig
        restFloorConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, floorConfig.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FloorConfig> floorConfigList = floorConfigRepository.findAll();
        assertThat(floorConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
