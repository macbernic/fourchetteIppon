package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FourchetteIpponApp;

import com.mycompany.myapp.domain.Contributeur;
import com.mycompany.myapp.repository.ContributeurRepository;
import com.mycompany.myapp.service.ContributeurService;
import com.mycompany.myapp.service.dto.ContributeurDTO;
import com.mycompany.myapp.service.mapper.ContributeurMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContributeurResource REST controller.
 *
 * @see ContributeurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FourchetteIpponApp.class)
public class ContributeurResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ContributeurRepository contributeurRepository;

    @Autowired
    private ContributeurMapper contributeurMapper;

    @Autowired
    private ContributeurService contributeurService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContributeurMockMvc;

    private Contributeur contributeur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContributeurResource contributeurResource = new ContributeurResource(contributeurService);
        this.restContributeurMockMvc = MockMvcBuilders.standaloneSetup(contributeurResource)
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
    public static Contributeur createEntity(EntityManager em) {
        Contributeur contributeur = new Contributeur()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .creationDate(DEFAULT_CREATION_DATE);
        return contributeur;
    }

    @Before
    public void initTest() {
        contributeur = createEntity(em);
    }

    @Test
    @Transactional
    public void createContributeur() throws Exception {
        int databaseSizeBeforeCreate = contributeurRepository.findAll().size();

        // Create the Contributeur
        ContributeurDTO contributeurDTO = contributeurMapper.toDto(contributeur);
        restContributeurMockMvc.perform(post("/api/contributeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributeurDTO)))
            .andExpect(status().isCreated());

        // Validate the Contributeur in the database
        List<Contributeur> contributeurList = contributeurRepository.findAll();
        assertThat(contributeurList).hasSize(databaseSizeBeforeCreate + 1);
        Contributeur testContributeur = contributeurList.get(contributeurList.size() - 1);
        assertThat(testContributeur.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContributeur.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testContributeur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContributeur.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testContributeur.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createContributeurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contributeurRepository.findAll().size();

        // Create the Contributeur with an existing ID
        contributeur.setId(1L);
        ContributeurDTO contributeurDTO = contributeurMapper.toDto(contributeur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributeurMockMvc.perform(post("/api/contributeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributeurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contributeur in the database
        List<Contributeur> contributeurList = contributeurRepository.findAll();
        assertThat(contributeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contributeurRepository.findAll().size();
        // set the field null
        contributeur.setEmail(null);

        // Create the Contributeur, which fails.
        ContributeurDTO contributeurDTO = contributeurMapper.toDto(contributeur);

        restContributeurMockMvc.perform(post("/api/contributeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributeurDTO)))
            .andExpect(status().isBadRequest());

        List<Contributeur> contributeurList = contributeurRepository.findAll();
        assertThat(contributeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContributeurs() throws Exception {
        // Initialize the database
        contributeurRepository.saveAndFlush(contributeur);

        // Get all the contributeurList
        restContributeurMockMvc.perform(get("/api/contributeurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))));
    }

    @Test
    @Transactional
    public void getContributeur() throws Exception {
        // Initialize the database
        contributeurRepository.saveAndFlush(contributeur);

        // Get the contributeur
        restContributeurMockMvc.perform(get("/api/contributeurs/{id}", contributeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contributeur.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingContributeur() throws Exception {
        // Get the contributeur
        restContributeurMockMvc.perform(get("/api/contributeurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContributeur() throws Exception {
        // Initialize the database
        contributeurRepository.saveAndFlush(contributeur);
        int databaseSizeBeforeUpdate = contributeurRepository.findAll().size();

        // Update the contributeur
        Contributeur updatedContributeur = contributeurRepository.findOne(contributeur.getId());
        updatedContributeur
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .creationDate(UPDATED_CREATION_DATE);
        ContributeurDTO contributeurDTO = contributeurMapper.toDto(updatedContributeur);

        restContributeurMockMvc.perform(put("/api/contributeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributeurDTO)))
            .andExpect(status().isOk());

        // Validate the Contributeur in the database
        List<Contributeur> contributeurList = contributeurRepository.findAll();
        assertThat(contributeurList).hasSize(databaseSizeBeforeUpdate);
        Contributeur testContributeur = contributeurList.get(contributeurList.size() - 1);
        assertThat(testContributeur.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContributeur.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContributeur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContributeur.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testContributeur.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingContributeur() throws Exception {
        int databaseSizeBeforeUpdate = contributeurRepository.findAll().size();

        // Create the Contributeur
        ContributeurDTO contributeurDTO = contributeurMapper.toDto(contributeur);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContributeurMockMvc.perform(put("/api/contributeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributeurDTO)))
            .andExpect(status().isCreated());

        // Validate the Contributeur in the database
        List<Contributeur> contributeurList = contributeurRepository.findAll();
        assertThat(contributeurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContributeur() throws Exception {
        // Initialize the database
        contributeurRepository.saveAndFlush(contributeur);
        int databaseSizeBeforeDelete = contributeurRepository.findAll().size();

        // Get the contributeur
        restContributeurMockMvc.perform(delete("/api/contributeurs/{id}", contributeur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contributeur> contributeurList = contributeurRepository.findAll();
        assertThat(contributeurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contributeur.class);
        Contributeur contributeur1 = new Contributeur();
        contributeur1.setId(1L);
        Contributeur contributeur2 = new Contributeur();
        contributeur2.setId(contributeur1.getId());
        assertThat(contributeur1).isEqualTo(contributeur2);
        contributeur2.setId(2L);
        assertThat(contributeur1).isNotEqualTo(contributeur2);
        contributeur1.setId(null);
        assertThat(contributeur1).isNotEqualTo(contributeur2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributeurDTO.class);
        ContributeurDTO contributeurDTO1 = new ContributeurDTO();
        contributeurDTO1.setId(1L);
        ContributeurDTO contributeurDTO2 = new ContributeurDTO();
        assertThat(contributeurDTO1).isNotEqualTo(contributeurDTO2);
        contributeurDTO2.setId(contributeurDTO1.getId());
        assertThat(contributeurDTO1).isEqualTo(contributeurDTO2);
        contributeurDTO2.setId(2L);
        assertThat(contributeurDTO1).isNotEqualTo(contributeurDTO2);
        contributeurDTO1.setId(null);
        assertThat(contributeurDTO1).isNotEqualTo(contributeurDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contributeurMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contributeurMapper.fromId(null)).isNull();
    }
}
