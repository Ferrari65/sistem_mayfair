package com.Rpg.sistem_mayfair.domain;

import com.Rpg.sistem_mayfair.domain.Enum.Genero;
import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personagens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_personagens;

    private String nome;

    private Integer idade;

    private String titulo;

    @Min(0)
    @Max(50)
    @Column(nullable = false)
    private Integer prestigio = 20;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String imageUrl;


    // ============================================================
    // FAMÍLIA
    // ============================================================

    @ManyToOne
    @JoinColumn(name = "id_familia")
    @JsonIgnoreProperties("personagens")
    private Familia familia;


    // ============================================================
    // DATA DE CRIAÇÃO
    // ============================================================

    @Column(updatable = false)
    private LocalDateTime createdAt;


    // ============================================================
    // SHAPE
    // ============================================================

    private String shape;


    // ============================================================
    // PLAYER
    // ============================================================

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonBackReference
    private Player player;


    // ============================================================
    // GÊNERO
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero = Genero.NAO_INFORMADO;


    // ============================================================
    // STATUS CIVIL
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCivil statusCivil = StatusCivil.SOLTEIRO;


    // ============================================================
    // HISTÓRICO DE PRESTÍGIO
    // ============================================================

    @OneToMany(
            mappedBy = "personagem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties("personagem")
    private List<HistoricoPrestigio> historicoPrestigio =
            new ArrayList<>();


    // ============================================================
    // JORNAIS
    // ============================================================

    @ManyToMany(mappedBy = "personagens")
    private List<JornalPostagem> jornais =
            new ArrayList<>();


    // ============================================================
    // PARCEIRO
    // ============================================================

    @ManyToOne
    @JoinColumn(name = "parceiro_id")
    @JsonIgnoreProperties({
            "parceiro",
            "historicoPrestigio",
            "player"
    })
    private Personagem parceiro;


    // ============================================================
    // DIAMANTE
    // ============================================================

    private Boolean diamanteTemporada = false;


    // ============================================================
    // CRIAÇÃO
    // ============================================================

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


    // ============================================================
    // ALTERAR PRESTÍGIO
    // ============================================================

    public void alterarPrestigio(int quantidade) {

        int novo = this.prestigio + quantidade;

        this.prestigio = Math.max(
                0,
                Math.min(50, novo)
        );
    }


    // ============================================================
    // REMOVER DOS JORNAIS ANTES DE EXCLUIR
    // ============================================================

    @PreRemove
    private void removerAssociacoesJornais() {

        if (jornais != null) {

            for (JornalPostagem jornal : jornais) {

                if (jornal.getPersonagens() != null) {
                    jornal.getPersonagens().remove(this);
                }
            }

            jornais.clear();
        }
    }
}