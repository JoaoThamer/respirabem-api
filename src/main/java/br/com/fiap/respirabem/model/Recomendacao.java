package br.com.fiap.respirabem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Recomendação de saúde para um grupo sensível em determinado nível de AQI. */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recomendacoes")
public class Recomendacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_id", nullable = false)
    private GrupoSensivel grupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_aqi", nullable = false, length = 20)
    private NivelAqi nivelAqi;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer prioridade;

    @Column(nullable = false)
    private Boolean ativa = Boolean.TRUE;
}
