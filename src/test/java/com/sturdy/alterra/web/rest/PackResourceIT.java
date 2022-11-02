package com.sturdy.alterra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sturdy.alterra.IntegrationTest;
import com.sturdy.alterra.domain.Pack;
import com.sturdy.alterra.repository.PackRepository;
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
 * Integration tests for the {@link PackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PackResourceIT {

    private static final String DEFAULT_PACK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PACK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DECK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DECK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_SETUP = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_SETUP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/packs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PackRepository packRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackMockMvc;

    private Pack pack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pack createEntity(EntityManager em) {
        Pack pack = new Pack().packName(DEFAULT_PACK_NAME).deckName(DEFAULT_DECK_NAME).configSetup(DEFAULT_CONFIG_SETUP);
        return pack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pack createUpdatedEntity(EntityManager em) {
        Pack pack = new Pack().packName(UPDATED_PACK_NAME).deckName(UPDATED_DECK_NAME).configSetup(UPDATED_CONFIG_SETUP);
        return pack;
    }

    @BeforeEach
    public void initTest() {
        pack = createEntity(em);
    }

    @Test
    @Transactional
    void createPack() throws Exception {
        int databaseSizeBeforeCreate = packRepository.findAll().size();
        // Create the Pack
        restPackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isCreated());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeCreate + 1);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getPackName()).isEqualTo(DEFAULT_PACK_NAME);
        assertThat(testPack.getDeckName()).isEqualTo(DEFAULT_DECK_NAME);
        assertThat(testPack.getConfigSetup()).isEqualTo(DEFAULT_CONFIG_SETUP);
    }

    @Test
    @Transactional
    void createPackWithExistingId() throws Exception {
        // Create the Pack with an existing ID
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeCreate = packRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPackNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = packRepository.findAll().size();
        // set the field null
        pack.setPackName(null);

        // Create the Pack, which fails.

        restPackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isBadRequest());

        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPacks() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        // Get all the packList
        restPackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pack.getId().toString())))
            .andExpect(jsonPath("$.[*].packName").value(hasItem(DEFAULT_PACK_NAME)))
            .andExpect(jsonPath("$.[*].deckName").value(hasItem(DEFAULT_DECK_NAME)))
            .andExpect(jsonPath("$.[*].configSetup").value(hasItem(DEFAULT_CONFIG_SETUP)));
    }

    @Test
    @Transactional
    void getPack() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        // Get the pack
        restPackMockMvc
            .perform(get(ENTITY_API_URL_ID, pack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pack.getId().toString()))
            .andExpect(jsonPath("$.packName").value(DEFAULT_PACK_NAME))
            .andExpect(jsonPath("$.deckName").value(DEFAULT_DECK_NAME))
            .andExpect(jsonPath("$.configSetup").value(DEFAULT_CONFIG_SETUP));
    }

    @Test
    @Transactional
    void getNonExistingPack() throws Exception {
        // Get the pack
        restPackMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPack() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // Update the pack
        Pack updatedPack = packRepository.findById(pack.getId()).get();
        // Disconnect from session so that the updates on updatedPack are not directly saved in db
        em.detach(updatedPack);
        updatedPack.packName(UPDATED_PACK_NAME).deckName(UPDATED_DECK_NAME).configSetup(UPDATED_CONFIG_SETUP);

        restPackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPack))
            )
            .andExpect(status().isOk());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getPackName()).isEqualTo(UPDATED_PACK_NAME);
        assertThat(testPack.getDeckName()).isEqualTo(UPDATED_DECK_NAME);
        assertThat(testPack.getConfigSetup()).isEqualTo(UPDATED_CONFIG_SETUP);
    }

    @Test
    @Transactional
    void putNonExistingPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackWithPatch() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // Update the pack using partial update
        Pack partialUpdatedPack = new Pack();
        partialUpdatedPack.setId(pack.getId());

        partialUpdatedPack.deckName(UPDATED_DECK_NAME);

        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPack))
            )
            .andExpect(status().isOk());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getPackName()).isEqualTo(DEFAULT_PACK_NAME);
        assertThat(testPack.getDeckName()).isEqualTo(UPDATED_DECK_NAME);
        assertThat(testPack.getConfigSetup()).isEqualTo(DEFAULT_CONFIG_SETUP);
    }

    @Test
    @Transactional
    void fullUpdatePackWithPatch() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // Update the pack using partial update
        Pack partialUpdatedPack = new Pack();
        partialUpdatedPack.setId(pack.getId());

        partialUpdatedPack.packName(UPDATED_PACK_NAME).deckName(UPDATED_DECK_NAME).configSetup(UPDATED_CONFIG_SETUP);

        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPack))
            )
            .andExpect(status().isOk());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getPackName()).isEqualTo(UPDATED_PACK_NAME);
        assertThat(testPack.getDeckName()).isEqualTo(UPDATED_DECK_NAME);
        assertThat(testPack.getConfigSetup()).isEqualTo(UPDATED_CONFIG_SETUP);
    }

    @Test
    @Transactional
    void patchNonExistingPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePack() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeDelete = packRepository.findAll().size();

        // Delete the pack
        restPackMockMvc
            .perform(delete(ENTITY_API_URL_ID, pack.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
