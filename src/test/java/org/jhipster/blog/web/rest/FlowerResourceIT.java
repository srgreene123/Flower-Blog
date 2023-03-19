package org.jhipster.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.jhipster.blog.IntegrationTest;
import org.jhipster.blog.domain.Flower;
import org.jhipster.blog.repository.FlowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FlowerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FlowerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SEASON = "AAAAAAAAAA";
    private static final String UPDATED_SEASON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_LINK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flowers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlowerRepository flowerRepository;

    @Mock
    private FlowerRepository flowerRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlowerMockMvc;

    private Flower flower;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flower createEntity(EntityManager em) {
        Flower flower = new Flower()
            .name(DEFAULT_NAME)
            .season(DEFAULT_SEASON)
            .description(DEFAULT_DESCRIPTION)
            .imageLink(DEFAULT_IMAGE_LINK);
        return flower;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flower createUpdatedEntity(EntityManager em) {
        Flower flower = new Flower()
            .name(UPDATED_NAME)
            .season(UPDATED_SEASON)
            .description(UPDATED_DESCRIPTION)
            .imageLink(UPDATED_IMAGE_LINK);
        return flower;
    }

    @BeforeEach
    public void initTest() {
        flower = createEntity(em);
    }

    @Test
    @Transactional
    void createFlower() throws Exception {
        int databaseSizeBeforeCreate = flowerRepository.findAll().size();
        // Create the Flower
        restFlowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flower)))
            .andExpect(status().isCreated());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeCreate + 1);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFlower.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testFlower.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlower.getImageLink()).isEqualTo(DEFAULT_IMAGE_LINK);
    }

    @Test
    @Transactional
    void createFlowerWithExistingId() throws Exception {
        // Create the Flower with an existing ID
        flower.setId(1L);

        int databaseSizeBeforeCreate = flowerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flower)))
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlowers() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList
        restFlowerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flower.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageLink").value(hasItem(DEFAULT_IMAGE_LINK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlowersWithEagerRelationshipsIsEnabled() throws Exception {
        when(flowerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlowerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(flowerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlowersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(flowerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlowerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(flowerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFlower() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get the flower
        restFlowerMockMvc
            .perform(get(ENTITY_API_URL_ID, flower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flower.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imageLink").value(DEFAULT_IMAGE_LINK));
    }

    @Test
    @Transactional
    void getNonExistingFlower() throws Exception {
        // Get the flower
        restFlowerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFlower() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Update the flower
        Flower updatedFlower = flowerRepository.findById(flower.getId()).get();
        // Disconnect from session so that the updates on updatedFlower are not directly saved in db
        em.detach(updatedFlower);
        updatedFlower.name(UPDATED_NAME).season(UPDATED_SEASON).description(UPDATED_DESCRIPTION).imageLink(UPDATED_IMAGE_LINK);

        restFlowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlower.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlower))
            )
            .andExpect(status().isOk());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFlower.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testFlower.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlower.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void putNonExistingFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flower.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flower))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flower))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flower)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlowerWithPatch() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Update the flower using partial update
        Flower partialUpdatedFlower = new Flower();
        partialUpdatedFlower.setId(flower.getId());

        partialUpdatedFlower.name(UPDATED_NAME).season(UPDATED_SEASON).imageLink(UPDATED_IMAGE_LINK);

        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlower.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlower))
            )
            .andExpect(status().isOk());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFlower.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testFlower.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlower.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void fullUpdateFlowerWithPatch() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Update the flower using partial update
        Flower partialUpdatedFlower = new Flower();
        partialUpdatedFlower.setId(flower.getId());

        partialUpdatedFlower.name(UPDATED_NAME).season(UPDATED_SEASON).description(UPDATED_DESCRIPTION).imageLink(UPDATED_IMAGE_LINK);

        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlower.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlower))
            )
            .andExpect(status().isOk());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFlower.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testFlower.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlower.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void patchNonExistingFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flower.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flower))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flower))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flower)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlower() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        int databaseSizeBeforeDelete = flowerRepository.findAll().size();

        // Delete the flower
        restFlowerMockMvc
            .perform(delete(ENTITY_API_URL_ID, flower.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
