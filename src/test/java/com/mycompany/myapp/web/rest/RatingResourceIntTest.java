package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FourchetteIpponApp;

import com.mycompany.myapp.domain.Rating;
import com.mycompany.myapp.repository.RatingRepository;
import com.mycompany.myapp.service.RatingService;
import com.mycompany.myapp.service.dto.RatingDTO;
import com.mycompany.myapp.service.mapper.RatingMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.FORK;
/**
 * Test class for the RatingResource REST controller.
 *
 * @see RatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FourchetteIpponApp.class)
public class RatingResourceIntTest {

    private static final FORK DEFAULT_FORK = FORK.AVOID;
    private static final FORK UPDATED_FORK = FORK.POOR;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRatingMockMvc;

    private Rating rating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RatingResource ratingResource = new RatingResource(ratingService);
        this.restRatingMockMvc = MockMvcBuilders.standaloneSetup(ratingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createEntity(EntityManager em) {
        Rating rating = new Rating()
            .fork(DEFAULT_FORK)
            .comment(DEFAULT_COMMENT);
        return rating;
    }

    @Before
    public void initTest() {
        rating = createEntity(em);
    }

    @Test
    @Transactional
    public void createRating() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);
        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isCreated());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate + 1);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getFork()).isEqualTo(DEFAULT_FORK);
        assertThat(testRating.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating with an existing ID
        rating.setId(1L);
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkForkIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setFork(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRatings() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get all the ratingList
        restRatingMockMvc.perform(get("/api/ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].fork").value(hasItem(DEFAULT_FORK.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rating.getId().intValue()))
            .andExpect(jsonPath("$.fork").value(DEFAULT_FORK.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRating() throws Exception {
        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findOne(rating.getId());
        updatedRating
            .fork(UPDATED_FORK)
            .comment(UPDATED_COMMENT);
        RatingDTO ratingDTO = ratingMapper.toDto(updatedRating);

        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getFork()).isEqualTo(UPDATED_FORK);
        assertThat(testRating.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isCreated());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);
        int databaseSizeBeforeDelete = ratingRepository.findAll().size();

        // Get the rating
        restRatingMockMvc.perform(delete("/api/ratings/{id}", rating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = new Rating();
        rating1.setId(1L);
        Rating rating2 = new Rating();
        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);
        rating2.setId(2L);
        assertThat(rating1).isNotEqualTo(rating2);
        rating1.setId(null);
        assertThat(rating1).isNotEqualTo(rating2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingDTO.class);
        RatingDTO ratingDTO1 = new RatingDTO();
        ratingDTO1.setId(1L);
        RatingDTO ratingDTO2 = new RatingDTO();
        assertThat(ratingDTO1).isNotEqualTo(ratingDTO2);
        ratingDTO2.setId(ratingDTO1.getId());
        assertThat(ratingDTO1).isEqualTo(ratingDTO2);
        ratingDTO2.setId(2L);
        assertThat(ratingDTO1).isNotEqualTo(ratingDTO2);
        ratingDTO1.setId(null);
        assertThat(ratingDTO1).isNotEqualTo(ratingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ratingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ratingMapper.fromId(null)).isNull();
    }
}