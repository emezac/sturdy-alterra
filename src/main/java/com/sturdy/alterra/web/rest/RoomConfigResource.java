package com.sturdy.alterra.web.rest;

import com.sturdy.alterra.domain.RoomConfig;
import com.sturdy.alterra.repository.RoomConfigRepository;
import com.sturdy.alterra.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sturdy.alterra.domain.RoomConfig}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RoomConfigResource {

    private final Logger log = LoggerFactory.getLogger(RoomConfigResource.class);

    private static final String ENTITY_NAME = "roomConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomConfigRepository roomConfigRepository;

    public RoomConfigResource(RoomConfigRepository roomConfigRepository) {
        this.roomConfigRepository = roomConfigRepository;
    }

    /**
     * {@code POST  /room-configs} : Create a new roomConfig.
     *
     * @param roomConfig the roomConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomConfig, or with status {@code 400 (Bad Request)} if the roomConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-configs")
    public ResponseEntity<RoomConfig> createRoomConfig(@RequestBody RoomConfig roomConfig) throws URISyntaxException {
        log.debug("REST request to save RoomConfig : {}", roomConfig);
        if (roomConfig.getId() != null) {
            throw new BadRequestAlertException("A new roomConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomConfig result = roomConfigRepository.save(roomConfig);
        return ResponseEntity
            .created(new URI("/api/room-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-configs/:id} : Updates an existing roomConfig.
     *
     * @param id the id of the roomConfig to save.
     * @param roomConfig the roomConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomConfig,
     * or with status {@code 400 (Bad Request)} if the roomConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-configs/{id}")
    public ResponseEntity<RoomConfig> updateRoomConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody RoomConfig roomConfig
    ) throws URISyntaxException {
        log.debug("REST request to update RoomConfig : {}, {}", id, roomConfig);
        if (roomConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoomConfig result = roomConfigRepository.save(roomConfig);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roomConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /room-configs/:id} : Partial updates given fields of an existing roomConfig, field will ignore if it is null
     *
     * @param id the id of the roomConfig to save.
     * @param roomConfig the roomConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomConfig,
     * or with status {@code 400 (Bad Request)} if the roomConfig is not valid,
     * or with status {@code 404 (Not Found)} if the roomConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/room-configs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomConfig> partialUpdateRoomConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody RoomConfig roomConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomConfig partially : {}, {}", id, roomConfig);
        if (roomConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomConfig> result = roomConfigRepository
            .findById(roomConfig.getId())
            .map(existingRoomConfig -> {
                if (roomConfig.getSetup() != null) {
                    existingRoomConfig.setSetup(roomConfig.getSetup());
                }
                if (roomConfig.getNumOfDoors() != null) {
                    existingRoomConfig.setNumOfDoors(roomConfig.getNumOfDoors());
                }
                if (roomConfig.getNumOfPrizes() != null) {
                    existingRoomConfig.setNumOfPrizes(roomConfig.getNumOfPrizes());
                }

                return existingRoomConfig;
            })
            .map(roomConfigRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roomConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /room-configs} : get all the roomConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomConfigs in body.
     */
    @GetMapping("/room-configs")
    public List<RoomConfig> getAllRoomConfigs() {
        log.debug("REST request to get all RoomConfigs");
        return roomConfigRepository.findAll();
    }

    /**
     * {@code GET  /room-configs/:id} : get the "id" roomConfig.
     *
     * @param id the id of the roomConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-configs/{id}")
    public ResponseEntity<RoomConfig> getRoomConfig(@PathVariable UUID id) {
        log.debug("REST request to get RoomConfig : {}", id);
        Optional<RoomConfig> roomConfig = roomConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(roomConfig);
    }

    /**
     * {@code DELETE  /room-configs/:id} : delete the "id" roomConfig.
     *
     * @param id the id of the roomConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-configs/{id}")
    public ResponseEntity<Void> deleteRoomConfig(@PathVariable UUID id) {
        log.debug("REST request to delete RoomConfig : {}", id);
        roomConfigRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
