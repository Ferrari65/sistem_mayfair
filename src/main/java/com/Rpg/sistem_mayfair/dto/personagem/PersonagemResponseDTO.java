package com.Rpg.sistem_mayfair.dto.personagem;

import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import com.Rpg.sistem_mayfair.domain.Personagem;

import java.util.ArrayList;
import java.util.List;

public record PersonagemResponseDTO(

        String id,
        String name,
        String family,
        String title,
        Integer prestige,
        String description,
        String imageUrl,
        List<Object> events,
        String shape,

        StatusCivil statusCivil

) {

    public PersonagemResponseDTO(Personagem personagem) {
        this(personagem, false);
    }

    public PersonagemResponseDTO(
            Personagem personagem,
            boolean isAdmin
    ) {
        this(
                String.valueOf(personagem.getId_personagens()),

                personagem.getNome(),

                personagem.getFamilia() != null
                        ? personagem.getFamilia().getNome()
                        : "Sem Família",

                personagem.getTitulo(),

                personagem.getPrestigio(),

                personagem.getDescricao(),

                personagem.getImageUrl(),

                new ArrayList<>(),

                isAdmin
                        ? personagem.getShape()
                        : null,

                personagem.getStatusCivil()
        );
    }
}