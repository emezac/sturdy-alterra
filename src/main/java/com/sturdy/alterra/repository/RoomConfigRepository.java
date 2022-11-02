package com.sturdy.alterra.repository;

import com.sturdy.alterra.domain.RoomConfig;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoomConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomConfigRepository extends JpaRepository<RoomConfig, UUID> {}
