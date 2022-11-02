package com.sturdy.alterra.repository;

import com.sturdy.alterra.domain.Map;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface MapRepositoryWithBagRelationships {
    Optional<Map> fetchBagRelationships(Optional<Map> map);

    List<Map> fetchBagRelationships(List<Map> maps);

    Page<Map> fetchBagRelationships(Page<Map> maps);
}
