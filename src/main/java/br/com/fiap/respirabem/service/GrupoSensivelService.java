package br.com.fiap.respirabem.service;

import br.com.fiap.respirabem.dto.request.GrupoSensivelRequest;
import br.com.fiap.respirabem.dto.response.GrupoSensivelResponse;
import br.com.fiap.respirabem.exception.ConflitoException;
import br.com.fiap.respirabem.exception.RecursoNaoEncontradoException;
import br.com.fiap.respirabem.model.GrupoSensivel;
import br.com.fiap.respirabem.repository.GrupoSensivelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GrupoSensivelService {

    private final GrupoSensivelRepository repository;
    private final ModelMapper mapper;

    public GrupoSensivelResponse criar(GrupoSensivelRequest request) {
        validarNomeUnico(request.getNome(), null);
        GrupoSensivel grupo = mapper.map(request, GrupoSensivel.class);
        return mapper.map(repository.save(grupo), GrupoSensivelResponse.class);
    }

    @Transactional(readOnly = true)
    public List<GrupoSensivelResponse> listar() {
        return repository.findAll().stream()
                .map(g -> mapper.map(g, GrupoSensivelResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public GrupoSensivelResponse buscar(Long id) {
        return mapper.map(obter(id), GrupoSensivelResponse.class);
    }

    public GrupoSensivelResponse atualizar(Long id, GrupoSensivelRequest request) {
        GrupoSensivel grupo = obter(id);
        validarNomeUnico(request.getNome(), id);
        mapper.map(request, grupo);
        return mapper.map(repository.save(grupo), GrupoSensivelResponse.class);
    }

    public void remover(Long id) {
        repository.delete(obter(id));
    }

    /** Usado por outros Services (nunca pelos Controllers). */
    GrupoSensivel obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Grupo sensível", id));
    }

    private void validarNomeUnico(String nome, Long idAtual) {
        repository.findByNomeIgnoreCase(nome.trim())
                .filter(existente -> !existente.getId().equals(idAtual))
                .ifPresent(e -> {
                    throw new ConflitoException("Já existe um grupo sensível com este nome.");
                });
    }
}
