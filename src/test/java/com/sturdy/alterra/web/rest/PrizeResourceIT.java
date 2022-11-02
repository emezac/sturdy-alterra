package com.sturdy.alterra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sturdy.alterra.IntegrationTest;
import com.sturdy.alterra.domain.Prize;
import com.sturdy.alterra.repository.PrizeRepository;
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
 * Integration tests for the {@link PrizeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrizeResourceIT {

    private static final String DEFAULT_PRIZE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRIZE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PIPS = 1L;
    private static final Long UPDATED_PIPS = 2L;

    private static final Instant DEFAULT_EXPIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/prizes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrizeMockMvc;

    private Prize prize;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prize createEntity(EntityManager em) {
        Prize prize = new Prize().prizeName(DEFAULT_PRIZE_NAME).pips(DEFAULT_PIPS).expireDate(DEFAULT_EXPIRE_DATE);
        return prize;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prize createUpdatedEntity(EntityManager em) {
        Prize prize = new Prize().prizeName(UPDATED_PRIZE_NAME).pips(UPDATED_PIPS).expireDate(UPDATED_EXPIRE_DATE);
        return prize;
    }

    @BeforeEach
    public void initTest() {
        prize = createEntity(em);
    }

    @Test
    @Transactional
    void createPrize() throws Exception {
        int databaseSizeBeforeCreate = prizeRepository.findAll().size();
        // Create the Prize
        restPrizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isCreated());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate + 1);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getPrizeName()).isEqualTo(DEFAULT_PRIZE_NAME);
        assertThat(testPrize.getPips()).isEqualTo(DEFAULT_PIPS);
        assertThat(testPrize.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void createPrizeWithExistingId() throws Exception {
        // Create the Prize with an existing ID
        prizeRepository.saveAndFlush(prize);

        int databaseSizeBeforeCreate = prizeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrizeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = prizeRepository.findAll().size();
        // set the field null
        prize.setPrizeName(null);

        // Create the Prize, which fails.

        restPrizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isBadRequest());

        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrizes() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList
        restPrizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prize.getId().toString())))
            .andExpect(jsonPath("$.[*].prizeName").value(hasItem(DEFAULT_PRIZE_NAME)))
            .andExpect(jsonPath("$.[*].pips").value(hasItem(DEFAULT_PIPS.intValue())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())));
    }

    @Test
    @Transactional
    void getPrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get the prize
        restPrizeMockMvc
            .perform(get(ENTITY_API_URL_ID, prize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prize.getId().toString()))
            .andExpect(jsonPath("$.prizeName").value(DEFAULT_PRIZE_NAME))
            .andExpect(jsonPath("$.pips").value(DEFAULT_PIPS.intValue()))
            .andExpect(jsonPath("$.expireDate").value(DEFAULT_EXPIRE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPrize() throws Exception {
        // Get the prize
        restPrizeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Update the prize
        Prize updatedPrize = prizeRepository.findById(prize.getId()).get();
        // Disconnect from session so that the updates on updatedPrize are not directly saved in db
        em.detach(updatedPrize);
        updatedPrize.prizeName(UPDATED_PRIZE_NAME).pips(UPDATED_PIPS).expireDate(UPDATED_EXPIRE_DATE);

        restPrizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrize.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrize))
            )
            .andExpect(status().isOk());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getPrizeName()).isEqualTo(UPDATED_PRIZE_NAME);
        assertThat(testPrize.getPips()).isEqualTo(UPDATED_PIPS);
        assertThat(testPrize.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();
        prize.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prize.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prize))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();
        prize.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prize))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();
        prize.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrizeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrizeWithPatch() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Update the prize using partial update
        Prize partialUpdatedPrize = new Prize();
        partialUpdatedPrize.setId(prize.getId());

        partialUpdatedPrize.prizeName(UPDATED_PRIZE_NAME).expireDate(UPDATED_EXPIRE_DATE);

        restPrizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrize.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrize))
            )
            .andExpect(status().isOk());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getPrizeName()).isEqualTo(UPDATED_PRIZE_NAME);
        assertThat(testPrize.getPips()).isEqualTo(DEFAULT_PIPS);
        assertThat(testPrize.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePrizeWithPatch() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Update the prize using partial update
        Prize partialUpdatedPrize = new Prize();
        partialUpdatedPrize.setId(prize.getId());

        partialUpdatedPrize.prizeName(UPDATED_PRIZE_NAME).pips(UPDATED_PIPS).expireDate(UPDATED_EXPIRE_DATE);

        restPrizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrize.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrize))
            )
            .andExpect(status().isOk());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getPrizeName()).isEqualTo(UPDATED_PRIZE_NAME);
        assertThat(testPrize.getPips()).isEqualTo(UPDATED_PIPS);
        assertThat(testPrize.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();
        prize.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prize.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prize))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();
        prize.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prize))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();
        prize.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrizeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        int databaseSizeBeforeDelete = prizeRepository.findAll().size();

        // Delete the prize
        restPrizeMockMvc
            .perform(delete(ENTITY_API_URL_ID, prize.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
