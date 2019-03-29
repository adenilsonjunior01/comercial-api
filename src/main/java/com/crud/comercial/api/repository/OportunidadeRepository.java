package com.crud.comercial.api.repository;

import com.crud.comercial.api.model.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {

    Optional<Oportunidade> findByDescricaoAndNomeProspecto(String descricao, String nomeProspecto);

}
