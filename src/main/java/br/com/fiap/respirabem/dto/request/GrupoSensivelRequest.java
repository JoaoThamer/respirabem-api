package br.com.fiap.respirabem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoSensivelRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 80, message = "O nome deve ter no máximo 80 caracteres.")
    @Schema(example = "Crianças")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    @Schema(example = "Respiram mais rápido e passam mais tempo ao ar livre.")
    private String descricao;

    @Size(max = 50, message = "A faixa etária deve ter no máximo 50 caracteres.")
    @Schema(example = "0 a 12 anos")
    private String faixaEtaria;

    @Size(max = 120, message = "O risco principal deve ter no máximo 120 caracteres.")
    @Schema(example = "Crises de asma e infecções respiratórias")
    private String riscoPrincipal;

    @NotNull(message = "O campo ativo é obrigatório.")
    @Schema(example = "true")
    private Boolean ativo = Boolean.TRUE;
}
