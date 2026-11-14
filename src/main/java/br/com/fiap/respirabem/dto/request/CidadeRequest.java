package br.com.fiap.respirabem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    @Schema(example = "São Paulo")
    private String nome;

    @NotBlank(message = "O estado (UF) é obrigatório.")
    @Pattern(regexp = "^[A-Za-z]{2}$", message = "O estado deve ter 2 letras (UF).")
    @Schema(example = "SP")
    private String estado;

    @NotNull(message = "A latitude é obrigatória.")
    @DecimalMin(value = "-90.0", message = "A latitude deve estar entre -90 e 90.")
    @DecimalMax(value = "90.0", message = "A latitude deve estar entre -90 e 90.")
    @Schema(example = "-23.5505")
    private Double latitude;

    @NotNull(message = "A longitude é obrigatória.")
    @DecimalMin(value = "-180.0", message = "A longitude deve estar entre -180 e 180.")
    @DecimalMax(value = "180.0", message = "A longitude deve estar entre -180 e 180.")
    @Schema(example = "-46.6333")
    private Double longitude;

    @PositiveOrZero(message = "A população não pode ser negativa.")
    @Schema(example = "12300000")
    private Integer populacao;
}
