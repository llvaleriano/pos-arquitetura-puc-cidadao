package br.gov.bomdestino.cidadao.web.rest;

import br.gov.bomdestino.cidadao.config.KafkaProperties;
import br.gov.bomdestino.cidadao.domain.Cidadao;
import br.gov.bomdestino.cidadao.repository.CidadaoRepository;
import br.gov.bomdestino.cidadao.repository.search.CidadaoSearchRepository;
import br.gov.bomdestino.cidadao.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@RestController
@RequestMapping("/api")
@Transactional
public class CidadaoResource {

    private final Logger log = LoggerFactory.getLogger(CidadaoResource.class);

    private static final String ENTITY_NAME = "Cidadao";
    private static final String TOPIC_NAME = "cidadaoCadastrado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CidadaoRepository cidadaoRepository;
    private final CidadaoSearchRepository cidadaoSearchRepository;
    private RestHighLevelClient esClient;

    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;

    public CidadaoResource(CidadaoRepository cidadaoRepository, CidadaoSearchRepository cidadaoSearchRepository, KafkaProperties kafkaProperties, RestHighLevelClient esClient) {
        this.cidadaoRepository = cidadaoRepository;
        this.cidadaoSearchRepository = cidadaoSearchRepository;
        this.kafkaProperties = kafkaProperties;
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        this.esClient = esClient;
    }

    @PostMapping("/cidadaos")
    public ResponseEntity<Cidadao> createCidadao(@Valid @RequestBody Cidadao cidadao) throws URISyntaxException, ExecutionException, InterruptedException {
        log.debug("Requisição REST para salvar um Cidadão : {}", cidadao);
        if (cidadao.getId() != null) {
            throw new BadRequestAlertException("Um novo cidadão não pode ter um ID vinculado", ENTITY_NAME, "idexists");
        }
        Cidadao result = cidadaoRepository.save(cidadao);
        cidadaoSearchRepository.save(result);

        RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC_NAME, cidadao.getId().toString())).get();
        log.debug("Mensagem enviada para o tópico {}, " +
            "partition {}, " +
            "offset {}, " +
            "em {}",
            metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()));

        return ResponseEntity.created(new URI("/api/cidadaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/cidadaos")
    public ResponseEntity<Cidadao> updateCidadao(@Valid @RequestBody Cidadao cidadao) throws URISyntaxException {
        log.debug("Requisição REST para atualizar um Cidadão : {}", cidadao);
        if (cidadao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cidadao result = cidadaoRepository.save(cidadao);
        cidadaoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cidadao.getId().toString()))
            .body(result);
    }

    @GetMapping("/cidadaos")
    public ResponseEntity<List<Cidadao>> getAllCidadaos(Pageable pageable) {
        log.debug("Requisição REST para recuperar uma página de Cidadãos");
        Page<Cidadao> page = cidadaoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_search/cidadaos")
    public ResponseEntity<List<Cidadao>> searchCidadaos(@RequestParam String query, Pageable pageable) throws IOException {
        log.debug("Requisição REST para pesquisar Cidadãos no elasticsearch. Query: {}", query);
        Page<Cidadao> page = cidadaoSearchRepository.search(queryStringQuery("*" + query + "*"), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/cidadaos/{id}")
    public ResponseEntity<Cidadao> getCidadao(@PathVariable Long id) {
        log.debug("Requisição REST para buscar um Cidadão : {}", id);
        Optional<Cidadao> cidadao = cidadaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cidadao);
    }

    @DeleteMapping("/cidadaos/{id}")
    public ResponseEntity<Void> deleteCidadao(@PathVariable Long id) {
        log.debug("Requisição REST para excluir um Cidadão : {}", id);
        cidadaoRepository.deleteById(id);
        cidadaoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
