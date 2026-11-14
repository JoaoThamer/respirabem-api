package br.com.fiap.respirabem.controller;

import br.com.fiap.respirabem.dto.request.CidadeRequest;
import br.com.fiap.respirabem.dto.response.CidadeResponse;
import br.com.fiap.respirabem.service.CidadeService;
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
@RequestMapping("/api/v1/cidades")
@RequiredArgsConstructor
@Tag(name = "Cidades", description = "Cidades monitoradas")
public class CidadeController {

    private final CidadeService service;

    @PostMapping
    @Operation(summary = "Cadastrar cidade")
    public ResponseEntity<CidadeResponse> criar(@Valid @RequestBody CidadeRequest request) {
        CidadeResponse criada = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(criada);
    }

    @GetMapping
    @Operation(summary = "Listar cidades")
    public ResponseEntity<List<CidadeResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cidade por id")
    public ResponseEntity<CidadeResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cidade")
    public ResponseEntity<CidadeResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CidadeRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover cidade")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
