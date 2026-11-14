package br.com.fiap.respirabem.repository;

import br.com.fiap.respirabem.model.NivelAqi;
import br.com.fiap.respirabem.model.Recomendacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {

    List<Recomendacao> findAllByOrderByPrioridadeAsc();

    List<Recomendacao> findByGrupoIdOrderByPrioridadeAsc(Long grupoId);

    List<Recomendacao> findByNivelAqiOrderByPrioridadeAsc(NivelAqi nivelAqi);

    List<Recomendacao> findByGrupoIdAndNivelAqiOrderByPrioridadeAsc(Long grupoId, NivelAqi nivelAqi);
}
