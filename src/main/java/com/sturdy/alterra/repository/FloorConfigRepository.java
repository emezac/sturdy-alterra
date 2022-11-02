package com.sturdy.alterra.repository;

import com.sturdy.alterra.domain.FloorConfig;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FloorConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FloorConfigRepository extends JpaRepository<FloorConfig, UUID> {}
