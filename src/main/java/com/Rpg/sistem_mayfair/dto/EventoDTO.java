package com.Rpg.sistem_mayfair.dto;

import java.time.LocalDateTime;

public record EventoDTO(
        Long id,
        String titulo,
        String descricao,
        Boolean finalizado,
        LocalDateTime createdAt,
        LocalDateTime finalizadoAt
) {
}