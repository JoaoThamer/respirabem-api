package br.com.fiap.respirabem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Cidade monitorada pelo RespiraBem. */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cidades",
        uniqueConstraints = @UniqueConstraint(name = "uk_cidades_nome_estado", columnNames = {"nome", "estado"}))
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Integer populacao;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "cidade", cascade = CascadeType.REMOVE)
    private List<LeituraAr> leituras = new ArrayList<>();

    @PrePersist
    void antesDeSalvar() {
        this.dataCadastro = LocalDateTime.now();
    }
}
