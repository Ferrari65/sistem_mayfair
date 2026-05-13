package com.Rpg.sistem_mayfair.dto.personagem;

import com.Rpg.sistem_mayfair.domain.Personagem;

public class PersonagemMapper {

    /**
     * ENTITY -> DTO
     */
    public static PersonagemDTO toDTO(Personagem p) {

        if (p == null) return null;

        PersonagemDTO dto = new PersonagemDTO();

        dto.setName(p.getNome());
        dto.setAge(p.getIdade());
        dto.setTitle(p.getTitulo());
        dto.setPrestige(p.getPrestigio());
        dto.setDescription(p.getDescricao());

        dto.setImageUrl(
                p.getImageUrl() != null
                        ? p.getImageUrl().trim()
                        : null
        );

        // =========================
        // FAMILY
        // =========================
        if (p.getFamilia() != null) {
            dto.setFamilyId(
                    p.getFamilia().getId()
            );
        }

        // =========================
        // NOVOS CAMPOS
        // =========================
        dto.setGenero(
                p.getGenero()
        );

        dto.setStatusCivil(
                p.getStatusCivil()
        );

        dto.setShape(
                p.getShape()
        );

        // =========================
        // PARCEIRO
        // =========================
        if (p.getParceiro() != null) {

            dto.setParceiroId(
                    p.getParceiro()
                            .getId_personagens()
            );
        }

        return dto;
    }

    /**
     * DTO -> ENTITY
     */
    public static Personagem toEntity(PersonagemDTO dto) {

        if (dto == null) return null;

        Personagem p = new Personagem();

        p.setNome(dto.getName());
        p.setIdade(dto.getAge());
        p.setTitulo(dto.getTitle());

        p.setPrestigio(
                dto.getPrestige() != null
                        ? dto.getPrestige()
                        : 20
        );

        p.setDescricao(dto.getDescription());

        p.setImageUrl(
                dto.getImageUrl()
        );

        // =========================
        // NOVOS CAMPOS
        // =========================
        p.setGenero(
                dto.getGenero()
        );

        p.setStatusCivil(
                dto.getStatusCivil()
        );

        p.setShape(
                dto.getShape()
        );

        return p;
    }

    /**
     * UPDATE ENTITY FROM DTO
     */
    public static void updateEntityFromDTO(
            PersonagemDTO dto,
            Personagem p
    ) {

        if (dto == null || p == null) return;

        if (dto.getName() != null) {
            p.setNome(dto.getName());
        }

        if (dto.getAge() != null) {
            p.setIdade(dto.getAge());
        }

        if (dto.getTitle() != null) {
            p.setTitulo(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            p.setDescricao(dto.getDescription());
        }

        if (dto.getPrestige() != null) {
            p.setPrestigio(dto.getPrestige());
        }

        if (dto.getImageUrl() != null) {
            p.setImageUrl(dto.getImageUrl());
        }

        // =========================
        // NOVOS CAMPOS
        // =========================
        if (dto.getGenero() != null) {
            p.setGenero(dto.getGenero());
        }

        if (dto.getStatusCivil() != null) {
            p.setStatusCivil(dto.getStatusCivil());
        }

        if (dto.getShape() != null) {
            p.setShape(dto.getShape());
        }
    }
}