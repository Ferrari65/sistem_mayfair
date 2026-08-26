package com.Rpg.sistem_mayfair.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "historico_prestigio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPrestigio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorico;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Integer pontos;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;


    // ============================================================
    // PERSONAGEM
    // ============================================================

    @ManyToOne
    @JoinColumn(
            name = "id_personagem",
            nullable = false
    )
    private Personagem personagem;


    // ============================================================
    // DATA DE CRIAÇÃO
    // ============================================================

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }
}