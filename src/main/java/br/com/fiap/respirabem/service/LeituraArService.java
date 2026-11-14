package br.com.fiap.respirabem.service;

import br.com.fiap.respirabem.dto.request.LeituraArRequest;
import br.com.fiap.respirabem.dto.response.LeituraArResponse;
import br.com.fiap.respirabem.exception.RecursoNaoEncontradoException;
import br.com.fiap.respirabem.model.LeituraAr;
import br.com.fiap.respirabem.model.NivelAqi;
import br.com.fiap.respirabem.repository.LeituraArRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeituraArService {

    private final LeituraArRepository repository;
    private final CidadeService cidadeService;
    private final ModelMapper mapper;

    public LeituraArResponse criar(LeituraArRequest request) {
        LeituraAr leitura = mapper.map(request, LeituraAr.class);
        preencher(leitura, request);
        return paraResponse(repository.save(leitura));
    }

    @Transactional(readOnly = true)
    public List<LeituraArResponse> listar(Long cidadeId) {
        List<LeituraAr> leituras = (cidadeId == null)
                ? repository.findAllByOrderByDataHoraMedicaoDesc()
                : repository.findByCidadeIdOrderByDataHoraMedicaoDesc(cidadeId);
        return leituras.stream().map(this::paraResponse).toList();
    }

    @Transactional(readOnly = true)
    public LeituraArResponse buscar(Long id) {
        return paraResponse(obter(id));
    }

    public LeituraArResponse atualizar(Long id, LeituraArRequest request) {
        LeituraAr leitura = obter(id);
        mapper.map(request, leitura);
        preencher(leitura, request);
        return paraResponse(repository.save(leitura));
    }

    public void remover(Long id) {
        repository.delete(obter(id));
    }

    /** Regras de negócio: cidade deve existir e o nível é sempre derivado do índice AQI. */
    private void preencher(LeituraAr leitura, LeituraArRequest request) {
        leitura.setCidade(cidadeService.obter(request.getCidadeId()));
        leitura.setNivelAqi(NivelAqi.deIndice(request.getIndiceAqi()));
    }

    private LeituraAr obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitura", id));
    }

    private LeituraArResponse paraResponse(LeituraAr leitura) {
        return mapper.map(leitura, LeituraArResponse.class);
    }
}
