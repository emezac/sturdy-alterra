package com.sturdy.alterra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sturdy.alterra.IntegrationTest;
import com.sturdy.alterra.domain.GameConfig;
import com.sturdy.alterra.repository.GameConfigRepository;
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
 * Integration tests for the {@link GameConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameConfigResourceIT {

    private static final Instant DEFAULT_SETUP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SETUP_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_FLOOR_CONFIG = 1L;
    private static final Long UPDATED_FLOOR_CONFIG = 2L;

    private static final Long DEFAULT_ROOM_CONFIG = 1L;
    private static final Long UPDATED_ROOM_CONFIG = 2L;

    private static final Instant DEFAULT_DATE_INIT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INIT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/game-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private GameConfigRepository gameConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameConfigMockMvc;

    private GameConfig gameConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameConfig createEntity(EntityManager em) {
        GameConfig gameConfig = new GameConfig()
            .setupDate(DEFAULT_SETUP_DATE)
            .floorConfig(DEFAULT_FLOOR_CONFIG)
            .roomConfig(DEFAULT_ROOM_CONFIG)
            .dateInit(DEFAULT_DATE_INIT)
            .dateEnd(DEFAULT_DATE_END);
        return gameConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameConfig createUpdatedEntity(EntityManager em) {
        GameConfig gameConfig = new GameConfig()
            .setupDate(UPDATED_SETUP_DATE)
            .floorConfig(UPDATED_FLOOR_CONFIG)
            .roomConfig(UPDATED_ROOM_CONFIG)
            .dateInit(UPDATED_DATE_INIT)
            .dateEnd(UPDATED_DATE_END);
        return gameConfig;
    }

    @BeforeEach
    public void initTest() {
        gameConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createGameConfig() throws Exception {
        int databaseSizeBeforeCreate = gameConfigRepository.findAll().size();
        // Create the GameConfig
        restGameConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameConfig)))
            .andExpect(status().isCreated());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeCreate + 1);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getSetupDate()).isEqualTo(DEFAULT_SETUP_DATE);
        assertThat(testGameConfig.getFloorConfig()).isEqualTo(DEFAULT_FLOOR_CONFIG);
        assertThat(testGameConfig.getRoomConfig()).isEqualTo(DEFAULT_ROOM_CONFIG);
        assertThat(testGameConfig.getDateInit()).isEqualTo(DEFAULT_DATE_INIT);
        assertThat(testGameConfig.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
    }

    @Test
    @Transactional
    void createGameConfigWithExistingId() throws Exception {
        // Create the GameConfig with an existing ID
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeCreate = gameConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameConfig)))
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameConfigs() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        // Get all the gameConfigList
        restGameConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameConfig.getId().toString())))
            .andExpect(jsonPath("$.[*].setupDate").value(hasItem(DEFAULT_SETUP_DATE.toString())))
            .andExpect(jsonPath("$.[*].floorConfig").value(hasItem(DEFAULT_FLOOR_CONFIG.intValue())))
            .andExpect(jsonPath("$.[*].roomConfig").value(hasItem(DEFAULT_ROOM_CONFIG.intValue())))
            .andExpect(jsonPath("$.[*].dateInit").value(hasItem(DEFAULT_DATE_INIT.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())));
    }

    @Test
    @Transactional
    void getGameConfig() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        // Get the gameConfig
        restGameConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, gameConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameConfig.getId().toString()))
            .andExpect(jsonPath("$.setupDate").value(DEFAULT_SETUP_DATE.toString()))
            .andExpect(jsonPath("$.floorConfig").value(DEFAULT_FLOOR_CONFIG.intValue()))
            .andExpect(jsonPath("$.roomConfig").value(DEFAULT_ROOM_CONFIG.intValue()))
            .andExpect(jsonPath("$.dateInit").value(DEFAULT_DATE_INIT.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGameConfig() throws Exception {
        // Get the gameConfig
        restGameConfigMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameConfig() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();

        // Update the gameConfig
        GameConfig updatedGameConfig = gameConfigRepository.findById(gameConfig.getId()).get();
        // Disconnect from session so that the updates on updatedGameConfig are not directly saved in db
        em.detach(updatedGameConfig);
        updatedGameConfig
            .setupDate(UPDATED_SETUP_DATE)
            .floorConfig(UPDATED_FLOOR_CONFIG)
            .roomConfig(UPDATED_ROOM_CONFIG)
            .dateInit(UPDATED_DATE_INIT)
            .dateEnd(UPDATED_DATE_END);

        restGameConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameConfig))
            )
            .andExpect(status().isOk());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getSetupDate()).isEqualTo(UPDATED_SETUP_DATE);
        assertThat(testGameConfig.getFloorConfig()).isEqualTo(UPDATED_FLOOR_CONFIG);
        assertThat(testGameConfig.getRoomConfig()).isEqualTo(UPDATED_ROOM_CONFIG);
        assertThat(testGameConfig.getDateInit()).isEqualTo(UPDATED_DATE_INIT);
        assertThat(testGameConfig.getDateEnd()).isEqualTo(UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void putNonExistingGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameConfigWithPatch() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();

        // Update the gameConfig using partial update
        GameConfig partialUpdatedGameConfig = new GameConfig();
        partialUpdatedGameConfig.setId(gameConfig.getId());

        partialUpdatedGameConfig
            .floorConfig(UPDATED_FLOOR_CONFIG)
            .roomConfig(UPDATED_ROOM_CONFIG)
            .dateInit(UPDATED_DATE_INIT)
            .dateEnd(UPDATED_DATE_END);

        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameConfig))
            )
            .andExpect(status().isOk());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getSetupDate()).isEqualTo(DEFAULT_SETUP_DATE);
        assertThat(testGameConfig.getFloorConfig()).isEqualTo(UPDATED_FLOOR_CONFIG);
        assertThat(testGameConfig.getRoomConfig()).isEqualTo(UPDATED_ROOM_CONFIG);
        assertThat(testGameConfig.getDateInit()).isEqualTo(UPDATED_DATE_INIT);
        assertThat(testGameConfig.getDateEnd()).isEqualTo(UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void fullUpdateGameConfigWithPatch() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();

        // Update the gameConfig using partial update
        GameConfig partialUpdatedGameConfig = new GameConfig();
        partialUpdatedGameConfig.setId(gameConfig.getId());

        partialUpdatedGameConfig
            .setupDate(UPDATED_SETUP_DATE)
            .floorConfig(UPDATED_FLOOR_CONFIG)
            .roomConfig(UPDATED_ROOM_CONFIG)
            .dateInit(UPDATED_DATE_INIT)
            .dateEnd(UPDATED_DATE_END);

        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameConfig))
            )
            .andExpect(status().isOk());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getSetupDate()).isEqualTo(UPDATED_SETUP_DATE);
        assertThat(testGameConfig.getFloorConfig()).isEqualTo(UPDATED_FLOOR_CONFIG);
        assertThat(testGameConfig.getRoomConfig()).isEqualTo(UPDATED_ROOM_CONFIG);
        assertThat(testGameConfig.getDateInit()).isEqualTo(UPDATED_DATE_INIT);
        assertThat(testGameConfig.getDateEnd()).isEqualTo(UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void patchNonExistingGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameConfig() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeDelete = gameConfigRepository.findAll().size();

        // Delete the gameConfig
        restGameConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameConfig.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
