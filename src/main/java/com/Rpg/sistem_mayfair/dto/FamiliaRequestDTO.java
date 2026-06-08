package com.Rpg.sistem_mayfair.dto;

public record FamiliaRequestDTO(
        String nome,
        String titulo,
        String dilema,
        String photoUrl,
        String matriarca,
        String patriarca,
        Integer limiteVagas
) { }