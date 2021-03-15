package br.gov.bomdestino.cidadao.repository.search;

import br.gov.bomdestino.cidadao.domain.Cidadao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Cidadao} entity.
 */
public interface CidadaoSearchRepository extends ElasticsearchRepository<Cidadao, Long> {
}
