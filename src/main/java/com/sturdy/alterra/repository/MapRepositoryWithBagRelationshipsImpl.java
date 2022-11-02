package com.sturdy.alterra.repository;

import com.sturdy.alterra.domain.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MapRepositoryWithBagRelationshipsImpl implements MapRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Map> fetchBagRelationships(Optional<Map> map) {
        return map.map(this::fetchTasks);
    }

    @Override
    public Page<Map> fetchBagRelationships(Page<Map> maps) {
        return new PageImpl<>(fetchBagRelationships(maps.getContent()), maps.getPageable(), maps.getTotalElements());
    }

    @Override
    public List<Map> fetchBagRelationships(List<Map> maps) {
        return Optional.of(maps).map(this::fetchTasks).orElse(Collections.emptyList());
    }

    Map fetchTasks(Map result) {
        return entityManager
            .createQuery("select map from Map map left join fetch map.tasks where map is :map", Map.class)
            .setParameter("map", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Map> fetchTasks(List<Map> maps) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, maps.size()).forEach(index -> order.put(maps.get(index).getId(), index));
        List<Map> result = entityManager
            .createQuery("select distinct map from Map map left join fetch map.tasks where map in :maps", Map.class)
            .setParameter("maps", maps)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
