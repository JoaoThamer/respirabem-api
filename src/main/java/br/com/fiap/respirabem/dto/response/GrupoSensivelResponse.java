package br.com.fiap.respirabem.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoSensivelResponse {

    private Long id;
    private String nome;
    private String descricao;
    private String faixaEtaria;
    private String riscoPrincipal;
    private Boolean ativo;
}
