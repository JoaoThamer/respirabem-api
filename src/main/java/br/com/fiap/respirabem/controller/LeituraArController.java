package br.com.fiap.respirabem.controller;

import br.com.fiap.respirabem.dto.request.LeituraArRequest;
import br.com.fiap.respirabem.dto.response.LeituraArResponse;
import br.com.fiap.respirabem.service.LeituraArService;
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
@RequestMapping("/api/v1/leituras")
@RequiredArgsConstructor
@Tag(name = "Leituras de ar", description = "Leituras da qualidade do ar por cidade")
public class LeituraArController {

    private final LeituraArService service;

    @PostMapping
    @Operation(summary = "Registrar leitura (o nível AQI é calculado a partir do índice)")
    public ResponseEntity<LeituraArResponse> criar(@Valid @RequestBody LeituraArRequest request) {
        LeituraArResponse criada = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(criada);
    }

    @GetMapping
    @Operation(summary = "Listar leituras (filtro opcional por cidadeId)")
    public ResponseEntity<List<LeituraArResponse>> listar(@RequestParam(required = false) Long cidadeId) {
        return ResponseEntity.ok(service.listar(cidadeId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar leitura por id")
    public ResponseEntity<LeituraArResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar leitura")
    public ResponseEntity<LeituraArResponse> atualizar(@PathVariable Long id, @Valid @RequestBody LeituraArRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover leitura")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
