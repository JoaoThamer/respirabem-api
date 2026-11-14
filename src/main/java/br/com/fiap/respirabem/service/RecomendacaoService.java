package br.com.fiap.respirabem.service;

import br.com.fiap.respirabem.dto.request.RecomendacaoRequest;
import br.com.fiap.respirabem.dto.response.RecomendacaoResponse;
import br.com.fiap.respirabem.exception.RecursoNaoEncontradoException;
import br.com.fiap.respirabem.exception.RegraNegocioException;
import br.com.fiap.respirabem.model.GrupoSensivel;
import br.com.fiap.respirabem.model.NivelAqi;
import br.com.fiap.respirabem.model.Recomendacao;
import br.com.fiap.respirabem.repository.RecomendacaoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecomendacaoService {

    private final RecomendacaoRepository repository;
    private final GrupoSensivelService grupoService;
    private final ModelMapper mapper;

    public RecomendacaoResponse criar(RecomendacaoRequest request) {
        Recomendacao recomendacao = mapper.map(request, Recomendacao.class);
        preencher(recomendacao, request);
        return paraResponse(repository.save(recomendacao));
    }

    @Transactional(readOnly = true)
    public List<RecomendacaoResponse> listar(Long grupoId, NivelAqi nivelAqi) {
        List<Recomendacao> lista;
        if (grupoId != null && nivelAqi != null) {
            lista = repository.findByGrupoIdAndNivelAqiOrderByPrioridadeAsc(grupoId, nivelAqi);
        } else if (grupoId != null) {
            lista = repository.findByGrupoIdOrderByPrioridadeAsc(grupoId);
        } else if (nivelAqi != null) {
            lista = repository.findByNivelAqiOrderByPrioridadeAsc(nivelAqi);
        } else {
            lista = repository.findAllByOrderByPrioridadeAsc();
        }
        return lista.stream().map(this::paraResponse).toList();
    }

    @Transactional(readOnly = true)
    public RecomendacaoResponse buscar(Long id) {
        return paraResponse(obter(id));
    }

    public RecomendacaoResponse atualizar(Long id, RecomendacaoRequest request) {
        Recomendacao recomendacao = obter(id);
        mapper.map(request, recomendacao);
        preencher(recomendacao, request);
        return paraResponse(repository.save(recomendacao));
    }

    public void remover(Long id) {
        repository.delete(obter(id));
    }

    /** Regra de negócio: não se pode ativar uma recomendação para um grupo inativo. */
    private void preencher(Recomendacao recomendacao, RecomendacaoRequest request) {
        GrupoSensivel grupo = grupoService.obter(request.getGrupoId());
        if (Boolean.TRUE.equals(request.getAtiva()) && !Boolean.TRUE.equals(grupo.getAtivo())) {
            throw new RegraNegocioException("Não é possível ativar uma recomendação para um grupo sensível inativo.");
        }
        recomendacao.setGrupo(grupo);
    }

    private Recomendacao obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recomendação", id));
    }

    private RecomendacaoResponse paraResponse(Recomendacao r) {
        return mapper.map(r, RecomendacaoResponse.class);
    }
}
