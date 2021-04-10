package br.gov.bomdestino.cidadao.web.rest;

import br.gov.bomdestino.cidadao.domain.Endereco;
import br.gov.bomdestino.cidadao.repository.EnderecoRepository;
import br.gov.bomdestino.cidadao.repository.search.EnderecoSearchRepository;
import br.gov.bomdestino.cidadao.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link br.gov.bomdestino.cidadao.domain.Endereco}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EnderecoResource {

    private final Logger log = LoggerFactory.getLogger(EnderecoResource.class);

    private static final String ENTITY_NAME = "cidadaoEndereco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnderecoRepository enderecoRepository;

    private final EnderecoSearchRepository enderecoSearchRepository;

    public EnderecoResource(EnderecoRepository enderecoRepository, EnderecoSearchRepository enderecoSearchRepository) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoSearchRepository = enderecoSearchRepository;
    }

    /**
     * {@code POST  /enderecos} : Create a new endereco.
     *
     * @param endereco the endereco to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new endereco, or with status {@code 400 (Bad Request)} if the endereco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enderecos")
    public ResponseEntity<Endereco> createEndereco(@RequestBody Endereco endereco) throws URISyntaxException {
        log.debug("REST request to save Endereco : {}", endereco);
        if (endereco.getId() != null) {
            throw new BadRequestAlertException("A new endereco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Endereco result = enderecoRepository.save(endereco);
        enderecoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enderecos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enderecos} : Updates an existing endereco.
     *
     * @param endereco the endereco to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endereco,
     * or with status {@code 400 (Bad Request)} if the endereco is not valid,
     * or with status {@code 500 (Internal Server Error)} if the endereco couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enderecos")
    public ResponseEntity<Endereco> updateEndereco(@RequestBody Endereco endereco) throws URISyntaxException {
        log.debug("REST request to update Endereco : {}", endereco);
        if (endereco.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Endereco result = enderecoRepository.save(endereco);
        enderecoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, endereco.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /enderecos} : get all the enderecos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enderecos in body.
     */
    @GetMapping("/enderecos")
    public List<Endereco> getAllEnderecos() {
        log.debug("REST request to get all Enderecos");
        return enderecoRepository.findAll();
    }

    /**
     * {@code GET  /enderecos/:id} : get the "id" endereco.
     *
     * @param id the id of the endereco to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the endereco, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enderecos/{id}")
    public ResponseEntity<Endereco> getEndereco(@PathVariable Long id) {
        log.debug("REST request to get Endereco : {}", id);
        Optional<Endereco> endereco = enderecoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(endereco);
    }

    /**
     * {@code DELETE  /enderecos/:id} : delete the "id" endereco.
     *
     * @param id the id of the endereco to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enderecos/{id}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable Long id) {
        log.debug("REST request to delete Endereco : {}", id);
        enderecoRepository.deleteById(id);
        enderecoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/enderecos?query=:query} : search for the endereco corresponding
     * to the query.
     *
     * @param query the query of the endereco search.
     * @return the result of the search.
     */
    @GetMapping("/_search/enderecos")
    public List<Endereco> searchEnderecos(@RequestParam String query) {
        log.debug("REST request to search Enderecos for query {}", query);
        return StreamSupport
            .stream(enderecoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
