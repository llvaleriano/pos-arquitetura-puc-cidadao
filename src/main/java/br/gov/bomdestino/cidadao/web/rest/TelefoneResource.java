package br.gov.bomdestino.cidadao.web.rest;

import br.gov.bomdestino.cidadao.domain.Telefone;
import br.gov.bomdestino.cidadao.repository.TelefoneRepository;
import br.gov.bomdestino.cidadao.repository.search.TelefoneSearchRepository;
import br.gov.bomdestino.cidadao.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing {@link br.gov.bomdestino.cidadao.domain.Telefone}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TelefoneResource {

    private final Logger log = LoggerFactory.getLogger(TelefoneResource.class);

    private static final String ENTITY_NAME = "cidadaoTelefone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TelefoneRepository telefoneRepository;

    private final TelefoneSearchRepository telefoneSearchRepository;

    public TelefoneResource(TelefoneRepository telefoneRepository, TelefoneSearchRepository telefoneSearchRepository) {
        this.telefoneRepository = telefoneRepository;
        this.telefoneSearchRepository = telefoneSearchRepository;
    }

    /**
     * {@code POST  /telefones} : Create a new telefone.
     *
     * @param telefone the telefone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new telefone, or with status {@code 400 (Bad Request)} if the telefone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/telefones")
    public ResponseEntity<Telefone> createTelefone(@Valid @RequestBody Telefone telefone) throws URISyntaxException {
        log.debug("REST request to save Telefone : {}", telefone);
        if (telefone.getId() != null) {
            throw new BadRequestAlertException("A new telefone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Telefone result = telefoneRepository.save(telefone);
        telefoneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/telefones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /telefones} : Updates an existing telefone.
     *
     * @param telefone the telefone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated telefone,
     * or with status {@code 400 (Bad Request)} if the telefone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the telefone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/telefones")
    public ResponseEntity<Telefone> updateTelefone(@Valid @RequestBody Telefone telefone) throws URISyntaxException {
        log.debug("REST request to update Telefone : {}", telefone);
        if (telefone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Telefone result = telefoneRepository.save(telefone);
        telefoneSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, telefone.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /telefones} : get all the telefones.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of telefones in body.
     */
    @GetMapping("/telefones")
    public List<Telefone> getAllTelefones() {
        log.debug("REST request to get all Telefones");
        return telefoneRepository.findAll();
    }

    /**
     * {@code GET  /telefones/:id} : get the "id" telefone.
     *
     * @param id the id of the telefone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the telefone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/telefones/{id}")
    public ResponseEntity<Telefone> getTelefone(@PathVariable Long id) {
        log.debug("REST request to get Telefone : {}", id);
        Optional<Telefone> telefone = telefoneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(telefone);
    }

    /**
     * {@code DELETE  /telefones/:id} : delete the "id" telefone.
     *
     * @param id the id of the telefone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/telefones/{id}")
    public ResponseEntity<Void> deleteTelefone(@PathVariable Long id) {
        log.debug("REST request to delete Telefone : {}", id);
        telefoneRepository.deleteById(id);
        telefoneSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/telefones?query=:query} : search for the telefone corresponding
     * to the query.
     *
     * @param query the query of the telefone search.
     * @return the result of the search.
     */
    @GetMapping("/_search/telefones")
    public List<Telefone> searchTelefones(@RequestParam String query) {
        log.debug("REST request to search Telefones for query {}", query);
        return StreamSupport
            .stream(telefoneSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
