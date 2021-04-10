package br.gov.bomdestino.cidadao.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.gov.bomdestino.cidadao.web.rest.TestUtil;

public class CidadaoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cidadao.class);
        Cidadao cidadao1 = new Cidadao();
        cidadao1.setId(1L);
        Cidadao cidadao2 = new Cidadao();
        cidadao2.setId(cidadao1.getId());
        assertThat(cidadao1).isEqualTo(cidadao2);
        cidadao2.setId(2L);
        assertThat(cidadao1).isNotEqualTo(cidadao2);
        cidadao1.setId(null);
        assertThat(cidadao1).isNotEqualTo(cidadao2);
    }
}
