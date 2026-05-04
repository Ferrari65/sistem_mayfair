package com.Rpg.sistem_mayfair.dto;

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
        List<Object> events
) {

    private static final String BASE_URL =
            "https://sistem-mayfair.onrender.com";

    public PersonagemResponseDTO(Personagem personagem) {
        this(
                String.valueOf(personagem.getId_personagens()),
                personagem.getNome(),
                personagem.getFamilia() != null
                        ? personagem.getFamilia().getNome()
                        : "Sem Família",
                personagem.getTitulo(),
                personagem.getPrestigio(),
                personagem.getDescricao(),
                normalizeImageUrl(personagem.getImageUrl()),

                new ArrayList<>()
        );
    }

    private static String normalizeImageUrl(String url) {
        if (url == null || url.isBlank()) return null;

        if (url.startsWith("http")) return url;

        return BASE_URL + url;
    }
}