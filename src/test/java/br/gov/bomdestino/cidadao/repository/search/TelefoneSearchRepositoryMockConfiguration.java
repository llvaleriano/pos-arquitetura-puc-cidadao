package br.gov.bomdestino.cidadao.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TelefoneSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TelefoneSearchRepositoryMockConfiguration {

    @MockBean
    private TelefoneSearchRepository mockTelefoneSearchRepository;

}
