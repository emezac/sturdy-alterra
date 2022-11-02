package com.sturdy.alterra.repository;

import com.sturdy.alterra.domain.Door;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Door entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoorRepository extends JpaRepository<Door, UUID> {}
