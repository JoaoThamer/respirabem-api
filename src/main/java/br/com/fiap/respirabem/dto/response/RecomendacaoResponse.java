package br.com.fiap.respirabem.dto.response;

import br.com.fiap.respirabem.model.NivelAqi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecomendacaoResponse {

    private Long id;
    private Long grupoId;
    private String grupoNome;
    private NivelAqi nivelAqi;
    private String titulo;
    private String descricao;
    private Integer prioridade;
    private Boolean ativa;
}
