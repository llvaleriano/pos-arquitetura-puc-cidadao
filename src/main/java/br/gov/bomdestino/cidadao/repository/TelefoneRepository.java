package br.gov.bomdestino.cidadao.repository;

import br.gov.bomdestino.cidadao.domain.Telefone;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Telefone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
