package com.sturdy.alterra.web.rest;

import com.sturdy.alterra.domain.Dungeon;
import com.sturdy.alterra.repository.DungeonRepository;
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
 * REST controller for managing {@link com.sturdy.alterra.domain.Dungeon}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DungeonResource {

    private final Logger log = LoggerFactory.getLogger(DungeonResource.class);

    private static final String ENTITY_NAME = "dungeon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DungeonRepository dungeonRepository;

    public DungeonResource(DungeonRepository dungeonRepository) {
        this.dungeonRepository = dungeonRepository;
    }

    /**
     * {@code POST  /dungeons} : Create a new dungeon.
     *
     * @param dungeon the dungeon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dungeon, or with status {@code 400 (Bad Request)} if the dungeon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dungeons")
    public ResponseEntity<Dungeon> createDungeon(@Valid @RequestBody Dungeon dungeon) throws URISyntaxException {
        log.debug("REST request to save Dungeon : {}", dungeon);
        if (dungeon.getId() != null) {
            throw new BadRequestAlertException("A new dungeon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dungeon result = dungeonRepository.save(dungeon);
        return ResponseEntity
            .created(new URI("/api/dungeons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dungeons/:id} : Updates an existing dungeon.
     *
     * @param id the id of the dungeon to save.
     * @param dungeon the dungeon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dungeon,
     * or with status {@code 400 (Bad Request)} if the dungeon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dungeon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dungeons/{id}")
    public ResponseEntity<Dungeon> updateDungeon(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody Dungeon dungeon
    ) throws URISyntaxException {
        log.debug("REST request to update Dungeon : {}, {}", id, dungeon);
        if (dungeon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dungeon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dungeonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dungeon result = dungeonRepository.save(dungeon);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dungeon.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dungeons/:id} : Partial updates given fields of an existing dungeon, field will ignore if it is null
     *
     * @param id the id of the dungeon to save.
     * @param dungeon the dungeon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dungeon,
     * or with status {@code 400 (Bad Request)} if the dungeon is not valid,
     * or with status {@code 404 (Not Found)} if the dungeon is not found,
     * or with status {@code 500 (Internal Server Error)} if the dungeon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dungeons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dungeon> partialUpdateDungeon(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody Dungeon dungeon
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dungeon partially : {}, {}", id, dungeon);
        if (dungeon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dungeon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dungeonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dungeon> result = dungeonRepository
            .findById(dungeon.getId())
            .map(existingDungeon -> {
                if (dungeon.getDungeonName() != null) {
                    existingDungeon.setDungeonName(dungeon.getDungeonName());
                }
                if (dungeon.getStartDate() != null) {
                    existingDungeon.setStartDate(dungeon.getStartDate());
                }
                if (dungeon.getEndDate() != null) {
                    existingDungeon.setEndDate(dungeon.getEndDate());
                }

                return existingDungeon;
            })
            .map(dungeonRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dungeon.getId().toString())
        );
    }

    /**
     * {@code GET  /dungeons} : get all the dungeons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dungeons in body.
     */
    @GetMapping("/dungeons")
    public List<Dungeon> getAllDungeons() {
        log.debug("REST request to get all Dungeons");
        return dungeonRepository.findAll();
    }

    /**
     * {@code GET  /dungeons/:id} : get the "id" dungeon.
     *
     * @param id the id of the dungeon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dungeon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dungeons/{id}")
    public ResponseEntity<Dungeon> getDungeon(@PathVariable UUID id) {
        log.debug("REST request to get Dungeon : {}", id);
        Optional<Dungeon> dungeon = dungeonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dungeon);
    }

    /**
     * {@code DELETE  /dungeons/:id} : delete the "id" dungeon.
     *
     * @param id the id of the dungeon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dungeons/{id}")
    public ResponseEntity<Void> deleteDungeon(@PathVariable UUID id) {
        log.debug("REST request to delete Dungeon : {}", id);
        dungeonRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
