package com.sturdy.alterra.repository;

import com.sturdy.alterra.domain.GameConfig;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameConfigRepository extends JpaRepository<GameConfig, UUID> {}
