package br.com.fiap.respirabem.dto.response;

import br.com.fiap.respirabem.model.NivelAqi;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LeituraArResponse {

    private Long id;
    private Long cidadeId;
    private String cidadeNome;
    private Integer indiceAqi;
    private NivelAqi nivelAqi;
    private Double pm25;
    private Double pm10;
    private Double ozonio;
    private Double dioxidoNitrogenio;
    private LocalDateTime dataHoraMedicao;
    private String fonte;
}
