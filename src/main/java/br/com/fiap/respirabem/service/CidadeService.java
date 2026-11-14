package br.com.fiap.respirabem.service;

import br.com.fiap.respirabem.dto.request.CidadeRequest;
import br.com.fiap.respirabem.dto.response.CidadeResponse;
import br.com.fiap.respirabem.exception.ConflitoException;
import br.com.fiap.respirabem.exception.RecursoNaoEncontradoException;
import br.com.fiap.respirabem.model.Cidade;
import br.com.fiap.respirabem.repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CidadeService {

    private final CidadeRepository repository;
    private final ModelMapper mapper;

    public CidadeResponse criar(CidadeRequest request) {
        validarDuplicidade(request, null);
        Cidade cidade = mapper.map(request, Cidade.class);
        cidade.setEstado(cidade.getEstado().toUpperCase());
        return mapper.map(repository.save(cidade), CidadeResponse.class);
    }

    @Transactional(readOnly = true)
    public List<CidadeResponse> listar() {
        return repository.findAll().stream()
                .map(c -> mapper.map(c, CidadeResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public CidadeResponse buscar(Long id) {
        return mapper.map(obter(id), CidadeResponse.class);
    }

    public CidadeResponse atualizar(Long id, CidadeRequest request) {
        Cidade cidade = obter(id);
        validarDuplicidade(request, id);
        mapper.map(request, cidade);
        cidade.setEstado(cidade.getEstado().toUpperCase());
        return mapper.map(repository.save(cidade), CidadeResponse.class);
    }

    public void remover(Long id) {
        repository.delete(obter(id));
    }

    /** Usado por outros Services (nunca pelos Controllers). */
    Cidade obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cidade", id));
    }

    private void validarDuplicidade(CidadeRequest request, Long idAtual) {
        repository.findByNomeIgnoreCaseAndEstadoIgnoreCase(request.getNome().trim(), request.getEstado().trim())
                .filter(existente -> !existente.getId().equals(idAtual))
                .ifPresent(e -> {
                    throw new ConflitoException("Já existe uma cidade cadastrada com este nome e estado.");
                });
    }
}
