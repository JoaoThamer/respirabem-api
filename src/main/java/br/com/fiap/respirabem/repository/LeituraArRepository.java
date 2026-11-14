package br.com.fiap.respirabem.repository;

import br.com.fiap.respirabem.model.LeituraAr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeituraArRepository extends JpaRepository<LeituraAr, Long> {

    List<LeituraAr> findAllByOrderByDataHoraMedicaoDesc();

    List<LeituraAr> findByCidadeIdOrderByDataHoraMedicaoDesc(Long cidadeId);
}
