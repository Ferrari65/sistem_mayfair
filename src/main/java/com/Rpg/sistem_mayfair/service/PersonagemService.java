package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.Player;
import com.Rpg.sistem_mayfair.dto.PersonagemDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonagemService {

    private final PersonagemRepository personagemRepository;
    private final FamiliaRepository familiaRepository;
    private final PlayerRepository playerRepository;

    // =========================
    // CREATE PERSONAGEM
    // =========================
    public Personagem criar(PersonagemDTO dto) {

        Personagem personagem = new Personagem();

        personagem.setNome(dto.getName());
        personagem.setIdade(dto.getAge());
        personagem.setTitulo(dto.getTitle());

        personagem.setPrestigio(
                dto.getPrestige() != null
                        ? dto.getPrestige()
                        : 20
        );

        personagem.setDescricao(dto.getDescription());
        personagem.setImageUrl(dto.getImageUrl());

        // =========================
        // FAMILY
        // =========================
        if (dto.getFamilyId() != null && dto.getFamilyId() > 0) {

            Familia familia = familiaRepository
                    .findById(dto.getFamilyId())
                    .orElseThrow(() ->
                            new RuntimeException("Família não encontrada")
                    );

            personagem.setFamilia(familia);

        } else {

            personagem.setFamilia(null);
        }

        return personagemRepository.save(personagem);
    }

    // =========================
    // LISTAR PERSONAGENS
    // =========================
    public List<PersonagemDTO> listar(boolean isAdmin) {

        return personagemRepository.findAll()
                .stream()
                .map(p -> toDTO(p, isAdmin))
                .toList();
    }

    // =========================
    // ATRIBUIR PLAYER
    // =========================
    public Personagem atribuirPlayer(
            Long personagemId,
            Long playerId
    ) {

        Personagem personagem = personagemRepository
                .findById(personagemId)
                .orElseThrow(() ->
                        new RuntimeException("Personagem não encontrado")
                );

        Player player = playerRepository
                .findById(playerId)
                .orElseThrow(() ->
                        new RuntimeException("Player não encontrado")
                );

        personagem.setPlayer(player);

        return personagemRepository.save(personagem);
    }

    // =========================
    // ENTITY -> DTO
    // =========================
    private PersonagemDTO toDTO(
            Personagem p,
            boolean isAdmin
    ) {

        PersonagemDTO dto = new PersonagemDTO();

        dto.setName(p.getNome());
        dto.setAge(p.getIdade());
        dto.setTitle(p.getTitulo());
        dto.setPrestige(p.getPrestigio());
        dto.setDescription(p.getDescricao());
        dto.setImageUrl(p.getImageUrl());

        if (p.getFamilia() != null) {
            dto.setFamilyId(p.getFamilia().getId());
        }

        if (isAdmin) {
            dto.setShape(p.getShape());
        } else {
            dto.setShape(null);
        }

        return dto;
    }
}