package com.Rpg.sistem_mayfair.dto.jornal;

import java.util.List;

public record JornalPostagemRequestDTO(
        String titulo,
        String noticia,
        List<Long> personagensIds,
        List<String> tags
) {
}
