package com.Rpg.sistem_mayfair.dto;

import com.Rpg.sistem_mayfair.domain.Personagem;

public record PersonagemResponseDTO(

        Long id,
        String name,
        String family,
        String title,
        Integer prestige,
        String description,
        String imageUrl

) {

    public PersonagemResponseDTO(Personagem personagem) {
        this(
                personagem.getId_personagens(),
                personagem.getNome(),
                personagem.getFamilia() != null
                        ? personagem.getFamilia().getNome()
                        : null,
                personagem.getTitulo(),
                personagem.getPrestigio(),
                personagem.getDescricao(),
                personagem.getFoto()
        );
    }
}
