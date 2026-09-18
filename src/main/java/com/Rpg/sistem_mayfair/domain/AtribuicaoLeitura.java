package com.Rpg.sistem_mayfair.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "tb_atribuicoes_leitura",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_atribuicao_evento",
                        columnNames = "evento_id"
                )
        }
)
public class AtribuicaoLeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    public AtribuicaoLeitura() {
    }

    public AtribuicaoLeitura(Evento evento, Player player) {
        this.evento = evento;
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public Evento getEvento() {
        return evento;
    }

    public Player getPlayer() {
        return player;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
