package com.Rpg.sistem_mayfair.domain.estabelecimento;

import com.Rpg.sistem_mayfair.domain.Personagem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitas_estabelecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitaEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * DESCRIÇÃO DA VISITA
     */
    @Column(columnDefinition = "TEXT")
    private String descricao;

    /*
     * DATA
     */
    private LocalDateTime dataVisita;

    /*
     * PERSONAGEM
     */
    @ManyToOne
    @JoinColumn(name = "personagem_id")
    @JsonIgnoreProperties({
            "historicoPrestigio",
            "player",
            "familia"
    })
    private Personagem personagem;

    /*
     * ESTABELECIMENTO
     */
    @ManyToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;

    @PrePersist
    public void prePersist() {
        this.dataVisita = LocalDateTime.now();
    }
}