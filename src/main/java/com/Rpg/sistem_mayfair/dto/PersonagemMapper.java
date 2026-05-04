package com.Rpg.sistem_mayfair.dto;

import com.Rpg.sistem_mayfair.domain.Personagem;

public class PersonagemMapper {

    public static PersonagemDTO toDTO(Personagem p) {
        PersonagemDTO dto = new PersonagemDTO();

        dto.setName(p.getNome());
        dto.setAge(p.getIdade());
        dto.setTitle(p.getTitulo());
        dto.setPrestige(p.getPrestigio());
        dto.setDescription(p.getDescricao());
        dto.setImageUrl(
                p.getImageUrl() != null ? p.getImageUrl().trim() : null
        );

        dto.setFamily(
                p.getFamilia() != null ? p.getFamilia().getNome() : "Sem Família"
        );

        return dto;
    }

    public static Personagem toEntity(PersonagemDTO dto) {
        Personagem p = new Personagem();

        p.setNome(dto.getName());
        p.setIdade(dto.getAge());
        p.setTitulo(dto.getTitle());
        p.setPrestigio(dto.getPrestige());
        p.setDescricao(dto.getDescription());
        p.setImageUrl(dto.getImageUrl());

        return p;
    }
}
