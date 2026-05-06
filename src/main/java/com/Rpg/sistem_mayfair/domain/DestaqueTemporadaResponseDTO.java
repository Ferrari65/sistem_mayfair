package com.Rpg.sistem_mayfair.domain;

import java.time.LocalDateTime;

public record DestaqueTemporadaResponseDTO(

        String id,
        String temporada,
        Long diamanteId,
        String diamanteNome,
        Long familiaId,
        String familiaNome,
        LocalDateTime criadoEm

) {
}
