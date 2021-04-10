package br.gov.bomdestino.cidadao.repository.search;

import br.gov.bomdestino.cidadao.domain.Telefone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Telefone} entity.
 */
public interface TelefoneSearchRepository extends ElasticsearchRepository<Telefone, Long> {
}
