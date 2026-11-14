package br.com.fiap.respirabem.repository;

import br.com.fiap.respirabem.model.GrupoSensivel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoSensivelRepository extends JpaRepository<GrupoSensivel, Long> {

    Optional<GrupoSensivel> findByNomeIgnoreCase(String nome);
}
