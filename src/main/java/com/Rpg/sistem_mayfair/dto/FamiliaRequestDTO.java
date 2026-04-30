package com.Rpg.sistem_mayfair.dto;

import jakarta.validation.constraints.NotBlank;

public record FamiliaRequestDTO(
        @NotBlank String nome,
        String titulo,
        String dilema,
        String photoUrl,
        String matriarca,
        String patriarca
) { }