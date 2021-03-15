package br.gov.bomdestino.cidadao.repository;

import br.gov.bomdestino.cidadao.domain.Cidadao;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Cidadao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {
}
