package org.jhipster.blog.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster.blog.domain.Flower;
import org.jhipster.blog.repository.FlowerRepository;
import org.jhipster.blog.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.jhipster.blog.domain.Flower}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FlowerResource {

    private final Logger log = LoggerFactory.getLogger(FlowerResource.class);

    private static final String ENTITY_NAME = "flower";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowerRepository flowerRepository;

    public FlowerResource(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    /**
     * {@code POST  /flowers} : Create a new flower.
     *
     * @param flower the flower to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flower, or with status {@code 400 (Bad Request)} if the flower has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flowers")
    public ResponseEntity<Flower> createFlower(@RequestBody Flower flower) throws URISyntaxException {
        log.debug("REST request to save Flower : {}", flower);
        if (flower.getId() != null) {
            throw new BadRequestAlertException("A new flower cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Flower result = flowerRepository.save(flower);
        return ResponseEntity
            .created(new URI("/api/flowers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flowers/:id} : Updates an existing flower.
     *
     * @param id the id of the flower to save.
     * @param flower the flower to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flower,
     * or with status {@code 400 (Bad Request)} if the flower is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flower couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flowers/{id}")
    public ResponseEntity<Flower> updateFlower(@PathVariable(value = "id", required = false) final Long id, @RequestBody Flower flower)
        throws URISyntaxException {
        log.debug("REST request to update Flower : {}, {}", id, flower);
        if (flower.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flower.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Flower result = flowerRepository.save(flower);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flower.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flowers/:id} : Partial updates given fields of an existing flower, field will ignore if it is null
     *
     * @param id the id of the flower to save.
     * @param flower the flower to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flower,
     * or with status {@code 400 (Bad Request)} if the flower is not valid,
     * or with status {@code 404 (Not Found)} if the flower is not found,
     * or with status {@code 500 (Internal Server Error)} if the flower couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flowers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Flower> partialUpdateFlower(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Flower flower
    ) throws URISyntaxException {
        log.debug("REST request to partial update Flower partially : {}, {}", id, flower);
        if (flower.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flower.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Flower> result = flowerRepository
            .findById(flower.getId())
            .map(existingFlower -> {
                if (flower.getName() != null) {
                    existingFlower.setName(flower.getName());
                }
                if (flower.getSeason() != null) {
                    existingFlower.setSeason(flower.getSeason());
                }
                if (flower.getDescription() != null) {
                    existingFlower.setDescription(flower.getDescription());
                }
                if (flower.getImageLink() != null) {
                    existingFlower.setImageLink(flower.getImageLink());
                }

                return existingFlower;
            })
            .map(flowerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flower.getId().toString())
        );
    }

    /**
     * {@code GET  /flowers} : get all the flowers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flowers in body.
     */
    @GetMapping("/flowers")
    public List<Flower> getAllFlowers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Flowers");
        if (eagerload) {
            return flowerRepository.findAllWithEagerRelationships();
        } else {
            return flowerRepository.findAll();
        }
    }

    /**
     * {@code GET  /flowers/:id} : get the "id" flower.
     *
     * @param id the id of the flower to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flower, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flowers/{id}")
    public ResponseEntity<Flower> getFlower(@PathVariable Long id) {
        log.debug("REST request to get Flower : {}", id);
        Optional<Flower> flower = flowerRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(flower);
    }

    /**
     * {@code DELETE  /flowers/:id} : delete the "id" flower.
     *
     * @param id the id of the flower to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flowers/{id}")
    public ResponseEntity<Void> deleteFlower(@PathVariable Long id) {
        log.debug("REST request to delete Flower : {}", id);
        flowerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
