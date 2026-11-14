package br.com.fiap.respirabem.repository;

import br.com.fiap.respirabem.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    Optional<Cidade> findByNomeIgnoreCaseAndEstadoIgnoreCase(String nome, String estado);
}
