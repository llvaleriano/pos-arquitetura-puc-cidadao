package br.gov.bomdestino.cidadao.web.rest;

import br.gov.bomdestino.cidadao.domain.Cidadao;
import br.gov.bomdestino.cidadao.repository.CidadaoRepository;
import br.gov.bomdestino.cidadao.repository.search.CidadaoSearchRepository;
import br.gov.bomdestino.cidadao.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link br.gov.bomdestino.cidadao.domain.Cidadao}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CidadaoResource {

    private final Logger log = LoggerFactory.getLogger(CidadaoResource.class);

    private static final String ENTITY_NAME = "cidadaoCidadao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CidadaoRepository cidadaoRepository;

    private final CidadaoSearchRepository cidadaoSearchRepository;

    public CidadaoResource(CidadaoRepository cidadaoRepository, CidadaoSearchRepository cidadaoSearchRepository) {
        this.cidadaoRepository = cidadaoRepository;
        this.cidadaoSearchRepository = cidadaoSearchRepository;
    }

    /**
     * {@code POST  /cidadaos} : Create a new cidadao.
     *
     * @param cidadao the cidadao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cidadao, or with status {@code 400 (Bad Request)} if the cidadao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cidadaos")
    public ResponseEntity<Cidadao> createCidadao(@Valid @RequestBody Cidadao cidadao) throws URISyntaxException {
        log.debug("REST request to save Cidadao : {}", cidadao);
        if (cidadao.getId() != null) {
            throw new BadRequestAlertException("A new cidadao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cidadao result = cidadaoRepository.save(cidadao);
        cidadaoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cidadaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cidadaos} : Updates an existing cidadao.
     *
     * @param cidadao the cidadao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cidadao,
     * or with status {@code 400 (Bad Request)} if the cidadao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cidadao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cidadaos")
    public ResponseEntity<Cidadao> updateCidadao(@Valid @RequestBody Cidadao cidadao) throws URISyntaxException {
        log.debug("REST request to update Cidadao : {}", cidadao);
        if (cidadao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cidadao result = cidadaoRepository.save(cidadao);
        cidadaoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cidadao.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cidadaos} : get all the cidadaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cidadaos in body.
     */
    @GetMapping("/cidadaos")
    public ResponseEntity<List<Cidadao>> getAllCidadaos(Pageable pageable) {
        log.debug("REST request to get a page of Cidadaos");
        Page<Cidadao> page = cidadaoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cidadaos/:id} : get the "id" cidadao.
     *
     * @param id the id of the cidadao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cidadao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cidadaos/{id}")
    public ResponseEntity<Cidadao> getCidadao(@PathVariable Long id) {
        log.debug("REST request to get Cidadao : {}", id);
        Optional<Cidadao> cidadao = cidadaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cidadao);
    }

    /**
     * {@code DELETE  /cidadaos/:id} : delete the "id" cidadao.
     *
     * @param id the id of the cidadao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cidadaos/{id}")
    public ResponseEntity<Void> deleteCidadao(@PathVariable Long id) {
        log.debug("REST request to delete Cidadao : {}", id);
        cidadaoRepository.deleteById(id);
        cidadaoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cidadaos?query=:query} : search for the cidadao corresponding
     * to the query.
     *
     * @param query the query of the cidadao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cidadaos")
    public ResponseEntity<List<Cidadao>> searchCidadaos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cidadaos for query {}", query);
        Page<Cidadao> page = cidadaoSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
