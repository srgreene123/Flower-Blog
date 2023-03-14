package org.jhipster.blog.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.jhipster.blog.domain.Flower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FlowerRepositoryWithBagRelationshipsImpl implements FlowerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Flower> fetchBagRelationships(Optional<Flower> flower) {
        return flower.map(this::fetchLocations);
    }

    @Override
    public Page<Flower> fetchBagRelationships(Page<Flower> flowers) {
        return new PageImpl<>(fetchBagRelationships(flowers.getContent()), flowers.getPageable(), flowers.getTotalElements());
    }

    @Override
    public List<Flower> fetchBagRelationships(List<Flower> flowers) {
        return Optional.of(flowers).map(this::fetchLocations).orElse(Collections.emptyList());
    }

    Flower fetchLocations(Flower result) {
        return entityManager
            .createQuery("select flower from Flower flower left join fetch flower.locations where flower is :flower", Flower.class)
            .setParameter("flower", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Flower> fetchLocations(List<Flower> flowers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, flowers.size()).forEach(index -> order.put(flowers.get(index).getId(), index));
        List<Flower> result = entityManager
            .createQuery(
                "select distinct flower from Flower flower left join fetch flower.locations where flower in :flowers",
                Flower.class
            )
            .setParameter("flowers", flowers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
