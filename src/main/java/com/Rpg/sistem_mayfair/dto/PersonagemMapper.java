package com.Rpg.sistem_mayfair.dto;

import com.Rpg.sistem_mayfair.domain.Personagem;

public class PersonagemMapper {

    /**
     * Converte a Entidade para DTO
     * Útil para enviar os dados para o formulário de edição no React
     */
    public static PersonagemDTO toDTO(Personagem p) {
        if (p == null) return null;

        PersonagemDTO dto = new PersonagemDTO();
        dto.setName(p.getNome());
        dto.setAge(p.getIdade());
        dto.setTitle(p.getTitulo());
        dto.setPrestige(p.getPrestigio());
        dto.setDescription(p.getDescricao());

        // Limpeza básica de strings
        dto.setImageUrl(p.getImageUrl() != null ? p.getImageUrl().trim() : null);

        // Mapeia apenas o ID da família, conforme sua estrutura de DTO
        if (p.getFamilia() != null) {
            dto.setFamilyId(p.getFamilia().getId());
        }

        return dto;
    }

    /**
     * Converte o DTO para uma nova Entidade
     * Útil para o método POST (Criação)
     */
    public static Personagem toEntity(PersonagemDTO dto) {
        if (dto == null) return null;

        Personagem p = new Personagem();
        p.setNome(dto.getName());
        p.setIdade(dto.getAge());
        p.setTitulo(dto.getTitle());
        p.setPrestigio(dto.getPrestige() != null ? dto.getPrestige() : 20);
        p.setDescricao(dto.getDescription());
        p.setImageUrl(dto.getImageUrl());

        // Nota: O vínculo com a Família (objeto) deve ser feito no Service/Controller
        // através do familiaRepository.findById(dto.getFamilyId())

        return p;
    }

    public static void updateEntityFromDTO(PersonagemDTO dto, Personagem p) {
        if (dto == null || p == null) return;

        if (dto.getName() != null) p.setNome(dto.getName());
        if (dto.getAge() != null) p.setIdade(dto.getAge());
        if (dto.getTitle() != null) p.setTitulo(dto.getTitle());
        if (dto.getDescription() != null) p.setDescricao(dto.getDescription());
        if (dto.getPrestige() != null) p.setPrestigio(dto.getPrestige());
        if (dto.getImageUrl() != null) p.setImageUrl(dto.getImageUrl());

    }
}