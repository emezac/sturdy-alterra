package com.sturdy.alterra.web.rest;

import com.sturdy.alterra.domain.Door;
import com.sturdy.alterra.repository.DoorRepository;
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
 * REST controller for managing {@link com.sturdy.alterra.domain.Door}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DoorResource {

    private final Logger log = LoggerFactory.getLogger(DoorResource.class);

    private static final String ENTITY_NAME = "door";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoorRepository doorRepository;

    public DoorResource(DoorRepository doorRepository) {
        this.doorRepository = doorRepository;
    }

    /**
     * {@code POST  /doors} : Create a new door.
     *
     * @param door the door to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new door, or with status {@code 400 (Bad Request)} if the door has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doors")
    public ResponseEntity<Door> createDoor(@Valid @RequestBody Door door) throws URISyntaxException {
        log.debug("REST request to save Door : {}", door);
        if (door.getId() != null) {
            throw new BadRequestAlertException("A new door cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Door result = doorRepository.save(door);
        return ResponseEntity
            .created(new URI("/api/doors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doors/:id} : Updates an existing door.
     *
     * @param id the id of the door to save.
     * @param door the door to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated door,
     * or with status {@code 400 (Bad Request)} if the door is not valid,
     * or with status {@code 500 (Internal Server Error)} if the door couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doors/{id}")
    public ResponseEntity<Door> updateDoor(@PathVariable(value = "id", required = false) final UUID id, @Valid @RequestBody Door door)
        throws URISyntaxException {
        log.debug("REST request to update Door : {}, {}", id, door);
        if (door.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, door.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Door result = doorRepository.save(door);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, door.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doors/:id} : Partial updates given fields of an existing door, field will ignore if it is null
     *
     * @param id the id of the door to save.
     * @param door the door to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated door,
     * or with status {@code 400 (Bad Request)} if the door is not valid,
     * or with status {@code 404 (Not Found)} if the door is not found,
     * or with status {@code 500 (Internal Server Error)} if the door couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Door> partialUpdateDoor(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody Door door
    ) throws URISyntaxException {
        log.debug("REST request to partial update Door partially : {}, {}", id, door);
        if (door.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, door.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Door> result = doorRepository
            .findById(door.getId())
            .map(existingDoor -> {
                if (door.getDoorName() != null) {
                    existingDoor.setDoorName(door.getDoorName());
                }

                return existingDoor;
            })
            .map(doorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, door.getId().toString())
        );
    }

    /**
     * {@code GET  /doors} : get all the doors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doors in body.
     */
    @GetMapping("/doors")
    public List<Door> getAllDoors() {
        log.debug("REST request to get all Doors");
        return doorRepository.findAll();
    }

    /**
     * {@code GET  /doors/:id} : get the "id" door.
     *
     * @param id the id of the door to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the door, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doors/{id}")
    public ResponseEntity<Door> getDoor(@PathVariable UUID id) {
        log.debug("REST request to get Door : {}", id);
        Optional<Door> door = doorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(door);
    }

    /**
     * {@code DELETE  /doors/:id} : delete the "id" door.
     *
     * @param id the id of the door to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doors/{id}")
    public ResponseEntity<Void> deleteDoor(@PathVariable UUID id) {
        log.debug("REST request to delete Door : {}", id);
        doorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
