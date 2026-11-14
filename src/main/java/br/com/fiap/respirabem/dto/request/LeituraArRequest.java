package br.com.fiap.respirabem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LeituraArRequest {

    @NotNull(message = "O id da cidade é obrigatório.")
    @Schema(example = "1")
    private Long cidadeId;

    @NotNull(message = "O índice AQI é obrigatório.")
    @Min(value = 0, message = "O índice AQI deve estar entre 0 e 500.")
    @Max(value = 500, message = "O índice AQI deve estar entre 0 e 500.")
    @Schema(example = "42")
    private Integer indiceAqi;

    @PositiveOrZero(message = "PM2.5 não pode ser negativo.")
    @Schema(example = "10.5")
    private Double pm25;

    @PositiveOrZero(message = "PM10 não pode ser negativo.")
    @Schema(example = "20.3")
    private Double pm10;

    @PositiveOrZero(message = "O ozônio não pode ser negativo.")
    @Schema(example = "55.0")
    private Double ozonio;

    @PositiveOrZero(message = "O dióxido de nitrogênio não pode ser negativo.")
    @Schema(example = "12.3")
    private Double dioxidoNitrogenio;

    @NotNull(message = "A data/hora da medição é obrigatória.")
    @PastOrPresent(message = "A data/hora da medição não pode estar no futuro.")
    @Schema(example = "2026-11-05T12:00:00")
    private LocalDateTime dataHoraMedicao;

    @Size(max = 100, message = "A fonte deve ter no máximo 100 caracteres.")
    @Schema(example = "Open-Meteo")
    private String fonte;
}
