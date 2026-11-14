package br.com.fiap.respirabem.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CidadeResponse {

    private Long id;
    private String nome;
    private String estado;
    private Double latitude;
    private Double longitude;
    private Integer populacao;
    private LocalDateTime dataCadastro;
}
