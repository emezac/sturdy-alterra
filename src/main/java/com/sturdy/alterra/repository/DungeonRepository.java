package com.sturdy.alterra.repository;

import com.sturdy.alterra.domain.Dungeon;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dungeon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DungeonRepository extends JpaRepository<Dungeon, UUID> {}
