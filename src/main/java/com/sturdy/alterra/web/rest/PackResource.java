package com.sturdy.alterra.web.rest;

import com.sturdy.alterra.domain.Pack;
import com.sturdy.alterra.repository.PackRepository;
import com.sturdy.alterra.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sturdy.alterra.domain.Pack}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PackResource {

    private final Logger log = LoggerFactory.getLogger(PackResource.class);

    private static final String ENTITY_NAME = "pack";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackRepository packRepository;

    public PackResource(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    /**
     * {@code POST  /packs} : Create a new pack.
     *
     * @param pack the pack to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pack, or with status {@code 400 (Bad Request)} if the pack has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/packs")
    public ResponseEntity<Pack> createPack(@Valid @RequestBody Pack pack) throws URISyntaxException {
        log.debug("REST request to save Pack : {}", pack);
        if (pack.getId() != null) {
            throw new BadRequestAlertException("A new pack cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pack result = packRepository.save(pack);
        return ResponseEntity
            .created(new URI("/api/packs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /packs/:id} : Updates an existing pack.
     *
     * @param id the id of the pack to save.
     * @param pack the pack to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pack,
     * or with status {@code 400 (Bad Request)} if the pack is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pack couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/packs/{id}")
    public ResponseEntity<Pack> updatePack(@PathVariable(value = "id", required = false) final UUID id, @Valid @RequestBody Pack pack)
        throws URISyntaxException {
        log.debug("REST request to update Pack : {}, {}", id, pack);
        if (pack.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pack.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pack result = packRepository.save(pack);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pack.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /packs/:id} : Partial updates given fields of an existing pack, field will ignore if it is null
     *
     * @param id the id of the pack to save.
     * @param pack the pack to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pack,
     * or with status {@code 400 (Bad Request)} if the pack is not valid,
     * or with status {@code 404 (Not Found)} if the pack is not found,
     * or with status {@code 500 (Internal Server Error)} if the pack couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/packs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pack> partialUpdatePack(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody Pack pack
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pack partially : {}, {}", id, pack);
        if (pack.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pack.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pack> result = packRepository
            .findById(pack.getId())
            .map(existingPack -> {
                if (pack.getPackName() != null) {
                    existingPack.setPackName(pack.getPackName());
                }
                if (pack.getDeckName() != null) {
                    existingPack.setDeckName(pack.getDeckName());
                }
                if (pack.getConfigSetup() != null) {
                    existingPack.setConfigSetup(pack.getConfigSetup());
                }

                return existingPack;
            })
            .map(packRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pack.getId().toString())
        );
    }

    /**
     * {@code GET  /packs} : get all the packs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packs in body.
     */
    @GetMapping("/packs")
    public List<Pack> getAllPacks() {
        log.debug("REST request to get all Packs");
        return packRepository.findAll();
    }

    /**
     * {@code GET  /packs/:id} : get the "id" pack.
     *
     * @param id the id of the pack to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pack, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/packs/{id}")
    public ResponseEntity<Pack> getPack(@PathVariable UUID id) {
        log.debug("REST request to get Pack : {}", id);
        Optional<Pack> pack = packRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pack);
    }

    /**
     * {@code DELETE  /packs/:id} : delete the "id" pack.
     *
     * @param id the id of the pack to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/packs/{id}")
    public ResponseEntity<Void> deletePack(@PathVariable UUID id) {
        log.debug("REST request to delete Pack : {}", id);
        packRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
