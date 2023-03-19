package org.jhipster.blog.repository;

import java.util.List;
import java.util.Optional;
import org.jhipster.blog.domain.Flower;
import org.springframework.data.domain.Page;

public interface FlowerRepositoryWithBagRelationships {
    Optional<Flower> fetchBagRelationships(Optional<Flower> flower);

    List<Flower> fetchBagRelationships(List<Flower> flowers);

    Page<Flower> fetchBagRelationships(Page<Flower> flowers);
}
