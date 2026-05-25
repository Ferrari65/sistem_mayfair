package com.Rpg.sistem_mayfair.dto.personagem;

import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import com.Rpg.sistem_mayfair.domain.HistoricoPrestigio;
import com.Rpg.sistem_mayfair.domain.Personagem;

import java.util.List;
import java.util.stream.Collectors;

public record PersonagemResponseDTO(

        String id,
        String name,
        String family,
        Long familyId,
        String title,
        Integer prestige,
        String description,
        String imageUrl,
        Integer age,
        String genero,
        StatusCivil statusCivil,
        Long parceiroId,
        String parceiroNome,
        List<HistoricoResponseDTO> historicoPrestigio,
        String shape,
        String createdAt

) {

    // DTO interno para cada entrada do histórico
    public record HistoricoResponseDTO(
            Long idHistorico,
            String descricao,
            Integer pontos,
            String createdAt
    ) {
        public static HistoricoResponseDTO from(HistoricoPrestigio h) {
            return new HistoricoResponseDTO(
                    h.getIdHistorico(),
                    h.getDescricao(),
                    h.getPontos(),
                    h.getCreatedAt() != null
                            ? h.getCreatedAt().toString()
                            : null
            );
        }
    }

    public PersonagemResponseDTO(Personagem personagem) {
        this(personagem, false);
    }

    public PersonagemResponseDTO(Personagem personagem, boolean isAdmin) {
        this(
                String.valueOf(personagem.getId_personagens()),

                personagem.getNome(),

                personagem.getFamilia() != null
                        ? personagem.getFamilia().getNome()
                        : null,

                personagem.getFamilia() != null
                        ? personagem.getFamilia().getId()
                        : null,

                personagem.getTitulo(),

                personagem.getPrestigio(),

                personagem.getDescricao(),

                personagem.getImageUrl(),

                personagem.getIdade(),

                personagem.getGenero() != null
                        ? personagem.getGenero().name()
                        : null,

                personagem.getStatusCivil(),

                personagem.getParceiro() != null
                        ? personagem.getParceiro().getId_personagens()
                        : null,

                personagem.getParceiro() != null
                        ? personagem.getParceiro().getNome()
                        : null,

                personagem.getHistoricoPrestigio() != null
                        ? personagem.getHistoricoPrestigio()
                        .stream()
                        .map(HistoricoResponseDTO::from)
                        .collect(Collectors.toList())
                        : List.of(),

                isAdmin
                        ? personagem.getShape()
                        : null,

                personagem.getCreatedAt() != null
                        ? personagem.getCreatedAt().toString()
                        : null
        );
    }
}