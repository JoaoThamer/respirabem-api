package br.com.fiap.respirabem.controller;

import br.com.fiap.respirabem.dto.request.RecomendacaoRequest;
import br.com.fiap.respirabem.dto.response.RecomendacaoResponse;
import br.com.fiap.respirabem.model.NivelAqi;
import br.com.fiap.respirabem.service.RecomendacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recomendacoes")
@RequiredArgsConstructor
@Tag(name = "Recomendações", description = "Orientações de saúde por grupo e nível de AQI")
public class RecomendacaoController {

    private final RecomendacaoService service;

    @PostMapping
    @Operation(summary = "Cadastrar recomendação")
    public ResponseEntity<RecomendacaoResponse> criar(@Valid @RequestBody RecomendacaoRequest request) {
        RecomendacaoResponse criada = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(criada);
    }

    @GetMapping
    @Operation(summary = "Listar recomendações (filtros opcionais: grupoId, nivelAqi)")
    public ResponseEntity<List<RecomendacaoResponse>> listar(@RequestParam(required = false) Long grupoId,
                                                             @RequestParam(required = false) NivelAqi nivelAqi) {
        return ResponseEntity.ok(service.listar(grupoId, nivelAqi));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recomendação por id")
    public ResponseEntity<RecomendacaoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar recomendação")
    public ResponseEntity<RecomendacaoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody RecomendacaoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover recomendação")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
