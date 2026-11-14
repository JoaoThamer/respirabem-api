package br.com.fiap.respirabem.controller;

import br.com.fiap.respirabem.dto.request.GrupoSensivelRequest;
import br.com.fiap.respirabem.dto.response.GrupoSensivelResponse;
import br.com.fiap.respirabem.service.GrupoSensivelService;
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
@RequestMapping("/api/v1/grupos-sensiveis")
@RequiredArgsConstructor
@Tag(name = "Grupos sensíveis", description = "Grupos mais vulneráveis à poluição do ar")
public class GrupoSensivelController {

    private final GrupoSensivelService service;

    @PostMapping
    @Operation(summary = "Cadastrar grupo sensível")
    public ResponseEntity<GrupoSensivelResponse> criar(@Valid @RequestBody GrupoSensivelRequest request) {
        GrupoSensivelResponse criado = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar grupos sensíveis")
    public ResponseEntity<List<GrupoSensivelResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar grupo sensível por id")
    public ResponseEntity<GrupoSensivelResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar grupo sensível")
    public ResponseEntity<GrupoSensivelResponse> atualizar(@PathVariable Long id, @Valid @RequestBody GrupoSensivelRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover grupo sensível")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
