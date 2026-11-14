package br.com.fiap.respirabem.dto.request;

import br.com.fiap.respirabem.model.NivelAqi;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecomendacaoRequest {

    @NotNull(message = "O id do grupo sensível é obrigatório.")
    @Schema(example = "1")
    private Long grupoId;

    @NotNull(message = "O nível AQI é obrigatório.")
    @Schema(example = "SENSIVEIS")
    private NivelAqi nivelAqi;

    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 120, message = "O título deve ter no máximo 120 caracteres.")
    @Schema(example = "Evite esforço intenso ao ar livre")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    @Schema(example = "Prefira brincadeiras em ambientes internos e observe tosse ou chiado.")
    private String descricao;

    @NotNull(message = "A prioridade é obrigatória.")
    @Min(value = 1, message = "A prioridade deve estar entre 1 e 5.")
    @Max(value = 5, message = "A prioridade deve estar entre 1 e 5.")
    @Schema(example = "1")
    private Integer prioridade;

    @NotNull(message = "O campo ativa é obrigatório.")
    @Schema(example = "true")
    private Boolean ativa = Boolean.TRUE;
}
