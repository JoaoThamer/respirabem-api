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

import java.time.LocalDateTime;

/** Leitura da qualidade do ar de uma cidade em um instante. */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "leituras_ar")
public class LeituraAr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    @Column(name = "indice_aqi", nullable = false)
    private Integer indiceAqi;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_aqi", nullable = false, length = 20)
    private NivelAqi nivelAqi;

    private Double pm25;

    private Double pm10;

    private Double ozonio;

    @Column(name = "dioxido_nitrogenio")
    private Double dioxidoNitrogenio;

    @Column(name = "data_hora_medicao", nullable = false)
    private LocalDateTime dataHoraMedicao;

    @Column(length = 100)
    private String fonte;
}
