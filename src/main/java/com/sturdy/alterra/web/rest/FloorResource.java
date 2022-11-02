package com.sturdy.alterra.web.rest;

import com.sturdy.alterra.domain.Floor;
import com.sturdy.alterra.repository.FloorRepository;
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
 * REST controller for managing {@link com.sturdy.alterra.domain.Floor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FloorResource {

    private final Logger log = LoggerFactory.getLogger(FloorResource.class);

    private static final String ENTITY_NAME = "floor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FloorRepository floorRepository;

    public FloorResource(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    /**
     * {@code POST  /floors} : Create a new floor.
     *
     * @param floor the floor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new floor, or with status {@code 400 (Bad Request)} if the floor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/floors")
    public ResponseEntity<Floor> createFloor(@Valid @RequestBody Floor floor) throws URISyntaxException {
        log.debug("REST request to save Floor : {}", floor);
        if (floor.getId() != null) {
            throw new BadRequestAlertException("A new floor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Floor result = floorRepository.save(floor);
        return ResponseEntity
            .created(new URI("/api/floors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /floors/:id} : Updates an existing floor.
     *
     * @param id the id of the floor to save.
     * @param floor the floor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floor,
     * or with status {@code 400 (Bad Request)} if the floor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the floor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/floors/{id}")
    public ResponseEntity<Floor> updateFloor(@PathVariable(value = "id", required = false) final UUID id, @Valid @RequestBody Floor floor)
        throws URISyntaxException {
        log.debug("REST request to update Floor : {}, {}", id, floor);
        if (floor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Floor result = floorRepository.save(floor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, floor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /floors/:id} : Partial updates given fields of an existing floor, field will ignore if it is null
     *
     * @param id the id of the floor to save.
     * @param floor the floor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floor,
     * or with status {@code 400 (Bad Request)} if the floor is not valid,
     * or with status {@code 404 (Not Found)} if the floor is not found,
     * or with status {@code 500 (Internal Server Error)} if the floor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/floors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Floor> partialUpdateFloor(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody Floor floor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Floor partially : {}, {}", id, floor);
        if (floor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Floor> result = floorRepository
            .findById(floor.getId())
            .map(existingFloor -> {
                if (floor.getFloorName() != null) {
                    existingFloor.setFloorName(floor.getFloorName());
                }

                return existingFloor;
            })
            .map(floorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, floor.getId().toString())
        );
    }

    /**
     * {@code GET  /floors} : get all the floors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of floors in body.
     */
    @GetMapping("/floors")
    public List<Floor> getAllFloors() {
        log.debug("REST request to get all Floors");
        return floorRepository.findAll();
    }

    /**
     * {@code GET  /floors/:id} : get the "id" floor.
     *
     * @param id the id of the floor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the floor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/floors/{id}")
    public ResponseEntity<Floor> getFloor(@PathVariable UUID id) {
        log.debug("REST request to get Floor : {}", id);
        Optional<Floor> floor = floorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(floor);
    }

    /**
     * {@code DELETE  /floors/:id} : delete the "id" floor.
     *
     * @param id the id of the floor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/floors/{id}")
    public ResponseEntity<Void> deleteFloor(@PathVariable UUID id) {
        log.debug("REST request to delete Floor : {}", id);
        floorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
