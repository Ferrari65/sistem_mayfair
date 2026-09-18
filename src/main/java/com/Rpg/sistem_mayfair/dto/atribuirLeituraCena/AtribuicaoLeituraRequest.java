package com.Rpg.sistem_mayfair.dto.atribuirLeituraCena;

public class AtribuicaoLeituraRequest {

    private Long eventoId;
    private Long playerId;

    public AtribuicaoLeituraRequest() {
    }

    public Long getEventoId() {
        return eventoId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}