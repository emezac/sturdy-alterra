package com.sturdy.alterra.web.rest;

import com.sturdy.alterra.domain.FloorConfig;
import com.sturdy.alterra.repository.FloorConfigRepository;
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
 * REST controller for managing {@link com.sturdy.alterra.domain.FloorConfig}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FloorConfigResource {

    private final Logger log = LoggerFactory.getLogger(FloorConfigResource.class);

    private static final String ENTITY_NAME = "floorConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FloorConfigRepository floorConfigRepository;

    public FloorConfigResource(FloorConfigRepository floorConfigRepository) {
        this.floorConfigRepository = floorConfigRepository;
    }

    /**
     * {@code POST  /floor-configs} : Create a new floorConfig.
     *
     * @param floorConfig the floorConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new floorConfig, or with status {@code 400 (Bad Request)} if the floorConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/floor-configs")
    public ResponseEntity<FloorConfig> createFloorConfig(@Valid @RequestBody FloorConfig floorConfig) throws URISyntaxException {
        log.debug("REST request to save FloorConfig : {}", floorConfig);
        if (floorConfig.getId() != null) {
            throw new BadRequestAlertException("A new floorConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FloorConfig result = floorConfigRepository.save(floorConfig);
        return ResponseEntity
            .created(new URI("/api/floor-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /floor-configs/:id} : Updates an existing floorConfig.
     *
     * @param id the id of the floorConfig to save.
     * @param floorConfig the floorConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floorConfig,
     * or with status {@code 400 (Bad Request)} if the floorConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the floorConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/floor-configs/{id}")
    public ResponseEntity<FloorConfig> updateFloorConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody FloorConfig floorConfig
    ) throws URISyntaxException {
        log.debug("REST request to update FloorConfig : {}, {}", id, floorConfig);
        if (floorConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floorConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floorConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FloorConfig result = floorConfigRepository.save(floorConfig);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, floorConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /floor-configs/:id} : Partial updates given fields of an existing floorConfig, field will ignore if it is null
     *
     * @param id the id of the floorConfig to save.
     * @param floorConfig the floorConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floorConfig,
     * or with status {@code 400 (Bad Request)} if the floorConfig is not valid,
     * or with status {@code 404 (Not Found)} if the floorConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the floorConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/floor-configs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FloorConfig> partialUpdateFloorConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody FloorConfig floorConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update FloorConfig partially : {}, {}", id, floorConfig);
        if (floorConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floorConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floorConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FloorConfig> result = floorConfigRepository
            .findById(floorConfig.getId())
            .map(existingFloorConfig -> {
                if (floorConfig.getSetup() != null) {
                    existingFloorConfig.setSetup(floorConfig.getSetup());
                }
                if (floorConfig.getNumOfRooms() != null) {
                    existingFloorConfig.setNumOfRooms(floorConfig.getNumOfRooms());
                }

                return existingFloorConfig;
            })
            .map(floorConfigRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, floorConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /floor-configs} : get all the floorConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of floorConfigs in body.
     */
    @GetMapping("/floor-configs")
    public List<FloorConfig> getAllFloorConfigs() {
        log.debug("REST request to get all FloorConfigs");
        return floorConfigRepository.findAll();
    }

    /**
     * {@code GET  /floor-configs/:id} : get the "id" floorConfig.
     *
     * @param id the id of the floorConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the floorConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/floor-configs/{id}")
    public ResponseEntity<FloorConfig> getFloorConfig(@PathVariable UUID id) {
        log.debug("REST request to get FloorConfig : {}", id);
        Optional<FloorConfig> floorConfig = floorConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(floorConfig);
    }

    /**
     * {@code DELETE  /floor-configs/:id} : delete the "id" floorConfig.
     *
     * @param id the id of the floorConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/floor-configs/{id}")
    public ResponseEntity<Void> deleteFloorConfig(@PathVariable UUID id) {
        log.debug("REST request to delete FloorConfig : {}", id);
        floorConfigRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
