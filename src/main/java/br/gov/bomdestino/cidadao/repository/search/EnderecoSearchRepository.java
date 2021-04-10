package br.gov.bomdestino.cidadao.repository.search;

import br.gov.bomdestino.cidadao.domain.Endereco;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Endereco} entity.
 */
public interface EnderecoSearchRepository extends ElasticsearchRepository<Endereco, Long> {
}
