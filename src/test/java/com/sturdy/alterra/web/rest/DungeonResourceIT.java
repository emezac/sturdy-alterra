package com.sturdy.alterra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sturdy.alterra.IntegrationTest;
import com.sturdy.alterra.domain.Dungeon;
import com.sturdy.alterra.repository.DungeonRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DungeonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DungeonResourceIT {

    private static final String DEFAULT_DUNGEON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DUNGEON_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/dungeons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DungeonRepository dungeonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDungeonMockMvc;

    private Dungeon dungeon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dungeon createEntity(EntityManager em) {
        Dungeon dungeon = new Dungeon().dungeonName(DEFAULT_DUNGEON_NAME).startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE);
        return dungeon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dungeon createUpdatedEntity(EntityManager em) {
        Dungeon dungeon = new Dungeon().dungeonName(UPDATED_DUNGEON_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        return dungeon;
    }

    @BeforeEach
    public void initTest() {
        dungeon = createEntity(em);
    }

    @Test
    @Transactional
    void createDungeon() throws Exception {
        int databaseSizeBeforeCreate = dungeonRepository.findAll().size();
        // Create the Dungeon
        restDungeonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dungeon)))
            .andExpect(status().isCreated());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeCreate + 1);
        Dungeon testDungeon = dungeonList.get(dungeonList.size() - 1);
        assertThat(testDungeon.getDungeonName()).isEqualTo(DEFAULT_DUNGEON_NAME);
        assertThat(testDungeon.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDungeon.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createDungeonWithExistingId() throws Exception {
        // Create the Dungeon with an existing ID
        dungeonRepository.saveAndFlush(dungeon);

        int databaseSizeBeforeCreate = dungeonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDungeonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dungeon)))
            .andExpect(status().isBadRequest());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDungeonNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dungeonRepository.findAll().size();
        // set the field null
        dungeon.setDungeonName(null);

        // Create the Dungeon, which fails.

        restDungeonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dungeon)))
            .andExpect(status().isBadRequest());

        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDungeons() throws Exception {
        // Initialize the database
        dungeonRepository.saveAndFlush(dungeon);

        // Get all the dungeonList
        restDungeonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dungeon.getId().toString())))
            .andExpect(jsonPath("$.[*].dungeonName").value(hasItem(DEFAULT_DUNGEON_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getDungeon() throws Exception {
        // Initialize the database
        dungeonRepository.saveAndFlush(dungeon);

        // Get the dungeon
        restDungeonMockMvc
            .perform(get(ENTITY_API_URL_ID, dungeon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dungeon.getId().toString()))
            .andExpect(jsonPath("$.dungeonName").value(DEFAULT_DUNGEON_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDungeon() throws Exception {
        // Get the dungeon
        restDungeonMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDungeon() throws Exception {
        // Initialize the database
        dungeonRepository.saveAndFlush(dungeon);

        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();

        // Update the dungeon
        Dungeon updatedDungeon = dungeonRepository.findById(dungeon.getId()).get();
        // Disconnect from session so that the updates on updatedDungeon are not directly saved in db
        em.detach(updatedDungeon);
        updatedDungeon.dungeonName(UPDATED_DUNGEON_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restDungeonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDungeon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDungeon))
            )
            .andExpect(status().isOk());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
        Dungeon testDungeon = dungeonList.get(dungeonList.size() - 1);
        assertThat(testDungeon.getDungeonName()).isEqualTo(UPDATED_DUNGEON_NAME);
        assertThat(testDungeon.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDungeon.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDungeon() throws Exception {
        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();
        dungeon.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDungeonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dungeon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dungeon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDungeon() throws Exception {
        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();
        dungeon.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDungeonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dungeon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDungeon() throws Exception {
        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();
        dungeon.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDungeonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dungeon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDungeonWithPatch() throws Exception {
        // Initialize the database
        dungeonRepository.saveAndFlush(dungeon);

        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();

        // Update the dungeon using partial update
        Dungeon partialUpdatedDungeon = new Dungeon();
        partialUpdatedDungeon.setId(dungeon.getId());

        partialUpdatedDungeon.startDate(UPDATED_START_DATE);

        restDungeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDungeon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDungeon))
            )
            .andExpect(status().isOk());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
        Dungeon testDungeon = dungeonList.get(dungeonList.size() - 1);
        assertThat(testDungeon.getDungeonName()).isEqualTo(DEFAULT_DUNGEON_NAME);
        assertThat(testDungeon.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDungeon.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDungeonWithPatch() throws Exception {
        // Initialize the database
        dungeonRepository.saveAndFlush(dungeon);

        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();

        // Update the dungeon using partial update
        Dungeon partialUpdatedDungeon = new Dungeon();
        partialUpdatedDungeon.setId(dungeon.getId());

        partialUpdatedDungeon.dungeonName(UPDATED_DUNGEON_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restDungeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDungeon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDungeon))
            )
            .andExpect(status().isOk());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
        Dungeon testDungeon = dungeonList.get(dungeonList.size() - 1);
        assertThat(testDungeon.getDungeonName()).isEqualTo(UPDATED_DUNGEON_NAME);
        assertThat(testDungeon.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDungeon.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDungeon() throws Exception {
        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();
        dungeon.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDungeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dungeon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dungeon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDungeon() throws Exception {
        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();
        dungeon.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDungeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dungeon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDungeon() throws Exception {
        int databaseSizeBeforeUpdate = dungeonRepository.findAll().size();
        dungeon.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDungeonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dungeon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dungeon in the database
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDungeon() throws Exception {
        // Initialize the database
        dungeonRepository.saveAndFlush(dungeon);

        int databaseSizeBeforeDelete = dungeonRepository.findAll().size();

        // Delete the dungeon
        restDungeonMockMvc
            .perform(delete(ENTITY_API_URL_ID, dungeon.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dungeon> dungeonList = dungeonRepository.findAll();
        assertThat(dungeonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
