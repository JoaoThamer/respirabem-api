package br.com.fiap.respirabem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** Grupo de pessoas mais sensíveis à poluição (crianças, idosos, gestantes...). */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "grupos_sensiveis",
        uniqueConstraints = @UniqueConstraint(name = "uk_grupos_sensiveis_nome", columnNames = "nome"))
public class GrupoSensivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "faixa_etaria", length = 50)
    private String faixaEtaria;

    @Column(name = "risco_principal", length = 120)
    private String riscoPrincipal;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.REMOVE)
    private List<Recomendacao> recomendacoes = new ArrayList<>();
}
