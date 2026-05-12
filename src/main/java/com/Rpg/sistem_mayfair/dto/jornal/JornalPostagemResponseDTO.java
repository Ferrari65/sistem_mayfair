package com.Rpg.sistem_mayfair.dto.jornal;

import com.Rpg.sistem_mayfair.dto.personagem.PersonagemResumoDTO;

import java.time.LocalDateTime;
import java.util.List;

public record JornalPostagemResponseDTO(

        Long id,
        String titulo,
        String noticia,
        Integer likes,
        LocalDateTime dataCriacao,
        List<PersonagemResumoDTO> personagens,
        List<JornalReacaoResponseDTO> reacoes
) {
}