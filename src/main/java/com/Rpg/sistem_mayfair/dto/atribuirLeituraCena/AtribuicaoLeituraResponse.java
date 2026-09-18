package com.Rpg.sistem_mayfair.dto.atribuirLeituraCena;

public class AtribuicaoLeituraResponse {

    private Long id;

    private Long eventoId;
    private String eventoTitulo;

    private Long playerId;
    private String playerNome;

    public AtribuicaoLeituraResponse(
            Long id,
            Long eventoId,
            String eventoTitulo,
            Long playerId,
            String playerNome
    ) {
        this.id = id;
        this.eventoId = eventoId;
        this.eventoTitulo = eventoTitulo;
        this.playerId = playerId;
        this.playerNome = playerNome;
    }

    public Long getId() {
        return id;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getPlayerNome() {
        return playerNome;
    }
}