package br.gov.bomdestino.cidadao.web.rest;

import br.gov.bomdestino.cidadao.CidadaoApp;
import br.gov.bomdestino.cidadao.domain.Cidadao;
import br.gov.bomdestino.cidadao.repository.CidadaoRepository;
import br.gov.bomdestino.cidadao.repository.search.CidadaoSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.gov.bomdestino.cidadao.domain.enumeration.Sexo;
/**
 * Integration tests for the {@link CidadaoResource} REST controller.
 */
@SpringBootTest(classes = CidadaoApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CidadaoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Sexo DEFAULT_SEXO = Sexo.MASCULINO;
    private static final Sexo UPDATED_SEXO = Sexo.FEMININO;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CidadaoRepository cidadaoRepository;

    /**
     * This repository is mocked in the br.gov.bomdestino.cidadao.repository.search test package.
     *
     * @see br.gov.bomdestino.cidadao.repository.search.CidadaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private CidadaoSearchRepository mockCidadaoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCidadaoMockMvc;

    private Cidadao cidadao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cidadao createEntity(EntityManager em) {
        Cidadao cidadao = new Cidadao()
            .nome(DEFAULT_NOME)
            .sexo(DEFAULT_SEXO)
            .email(DEFAULT_EMAIL)
            .nascimento(DEFAULT_NASCIMENTO);
        return cidadao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cidadao createUpdatedEntity(EntityManager em) {
        Cidadao cidadao = new Cidadao()
            .nome(UPDATED_NOME)
            .sexo(UPDATED_SEXO)
            .email(UPDATED_EMAIL)
            .nascimento(UPDATED_NASCIMENTO);
        return cidadao;
    }

    @BeforeEach
    public void initTest() {
        cidadao = createEntity(em);
    }

    @Test
    @Transactional
    public void createCidadao() throws Exception {
        int databaseSizeBeforeCreate = cidadaoRepository.findAll().size();
        // Create the Cidadao
        restCidadaoMockMvc.perform(post("/api/cidadaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cidadao)))
            .andExpect(status().isCreated());

        // Validate the Cidadao in the database
        List<Cidadao> cidadaoList = cidadaoRepository.findAll();
        assertThat(cidadaoList).hasSize(databaseSizeBeforeCreate + 1);
        Cidadao testCidadao = cidadaoList.get(cidadaoList.size() - 1);
        assertThat(testCidadao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCidadao.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testCidadao.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCidadao.getNascimento()).isEqualTo(DEFAULT_NASCIMENTO);

        // Validate the Cidadao in Elasticsearch
        verify(mockCidadaoSearchRepository, times(1)).save(testCidadao);
    }

    @Test
    @Transactional
    public void createCidadaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cidadaoRepository.findAll().size();

        // Create the Cidadao with an existing ID
        cidadao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCidadaoMockMvc.perform(post("/api/cidadaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cidadao)))
            .andExpect(status().isBadRequest());

        // Validate the Cidadao in the database
        List<Cidadao> cidadaoList = cidadaoRepository.findAll();
        assertThat(cidadaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cidadao in Elasticsearch
        verify(mockCidadaoSearchRepository, times(0)).save(cidadao);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cidadaoRepository.findAll().size();
        // set the field null
        cidadao.setNome(null);

        // Create the Cidadao, which fails.


        restCidadaoMockMvc.perform(post("/api/cidadaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cidadao)))
            .andExpect(status().isBadRequest());

        List<Cidadao> cidadaoList = cidadaoRepository.findAll();
        assertThat(cidadaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = cidadaoRepository.findAll().size();
        // set the field null
        cidadao.setEmail(null);

        // Create the Cidadao, which fails.


        restCidadaoMockMvc.perform(post("/api/cidadaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cidadao)))
            .andExpect(status().isBadRequest());

        List<Cidadao> cidadaoList = cidadaoRepository.findAll();
        assertThat(cidadaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCidadaos() throws Exception {
        // Initialize the database
        cidadaoRepository.saveAndFlush(cidadao);

        // Get all the cidadaoList
        restCidadaoMockMvc.perform(get("/api/cidadaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cidadao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].nascimento").value(hasItem(DEFAULT_NASCIMENTO.toString())));
    }
    
    @Test
    @Transactional
    public void getCidadao() throws Exception {
        // Initialize the database
        cidadaoRepository.saveAndFlush(cidadao);

        // Get the cidadao
        restCidadaoMockMvc.perform(get("/api/cidadaos/{id}", cidadao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cidadao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.nascimento").value(DEFAULT_NASCIMENTO.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCidadao() throws Exception {
        // Get the cidadao
        restCidadaoMockMvc.perform(get("/api/cidadaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCidadao() throws Exception {
        // Initialize the database
        cidadaoRepository.saveAndFlush(cidadao);

        int databaseSizeBeforeUpdate = cidadaoRepository.findAll().size();

        // Update the cidadao
        Cidadao updatedCidadao = cidadaoRepository.findById(cidadao.getId()).get();
        // Disconnect from session so that the updates on updatedCidadao are not directly saved in db
        em.detach(updatedCidadao);
        updatedCidadao
            .nome(UPDATED_NOME)
            .sexo(UPDATED_SEXO)
            .email(UPDATED_EMAIL)
            .nascimento(UPDATED_NASCIMENTO);

        restCidadaoMockMvc.perform(put("/api/cidadaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCidadao)))
            .andExpect(status().isOk());

        // Validate the Cidadao in the database
        List<Cidadao> cidadaoList = cidadaoRepository.findAll();
        assertThat(cidadaoList).hasSize(databaseSizeBeforeUpdate);
        Cidadao testCidadao = cidadaoList.get(cidadaoList.size() - 1);
        assertThat(testCidadao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCidadao.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testCidadao.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCidadao.getNascimento()).isEqualTo(UPDATED_NASCIMENTO);

        // Validate the Cidadao in Elasticsearch
        verify(mockCidadaoSearchRepository, times(1)).save(testCidadao);
    }

    @Test
    @Transactional
    public void updateNonExistingCidadao() throws Exception {
        int databaseSizeBeforeUpdate = cidadaoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCidadaoMockMvc.perform(put("/api/cidadaos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cidadao)))
            .andExpect(status().isBadRequest());

        // Validate the Cidadao in the database
        List<Cidadao> cidadaoList = cidadaoRepository.findAll();
        assertThat(cidadaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cidadao in Elasticsearch
        verify(mockCidadaoSearchRepository, times(0)).save(cidadao);
    }

    @Test
    @Transactional
    public void deleteCidadao() throws Exception {
        // Initialize the database
        cidadaoRepository.saveAndFlush(cidadao);

        int databaseSizeBeforeDelete = cidadaoRepository.findAll().size();

        // Delete the cidadao
        restCidadaoMockMvc.perform(delete("/api/cidadaos/{id}", cidadao.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cidadao> cidadaoList = cidadaoRepository.findAll();
        assertThat(cidadaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cidadao in Elasticsearch
        verify(mockCidadaoSearchRepository, times(1)).deleteById(cidadao.getId());
    }

    @Test
    @Transactional
    public void searchCidadao() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cidadaoRepository.saveAndFlush(cidadao);
        when(mockCidadaoSearchRepository.search(queryStringQuery("id:" + cidadao.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cidadao), PageRequest.of(0, 1), 1));

        // Search the cidadao
        restCidadaoMockMvc.perform(get("/api/_search/cidadaos?query=id:" + cidadao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cidadao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].nascimento").value(hasItem(DEFAULT_NASCIMENTO.toString())));
    }
}
