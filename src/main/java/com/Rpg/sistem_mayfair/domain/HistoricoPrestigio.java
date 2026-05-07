package com.Rpg.sistem_mayfair.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_prestigio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPrestigio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorico;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Integer pontos;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_personagem")
    private Personagem personagem;
}