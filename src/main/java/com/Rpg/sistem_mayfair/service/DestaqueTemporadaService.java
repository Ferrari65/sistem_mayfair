package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.*;
import com.Rpg.sistem_mayfair.repository.DestaqueTemporadaRepository;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestaqueTemporadaService {

    private final DestaqueTemporadaRepository destaqueRepository;
    private final PersonagemRepository personagemRepository;
    private final FamiliaRepository familiaRepository;

    @Transactional
    public DestaqueTemporadaResponseDTO criar(DestaqueTemporadaRequestDTO dto) {

        DestaqueTemporada destaque =
                destaqueRepository.findByTemporada(dto.temporada())
                        .orElse(new DestaqueTemporada());

        destaque.setTemporada(dto.temporada());

        Personagem diamante = null;
        Familia familia = null;

        if (dto.diamanteId() != null) {
            diamante = personagemRepository.findById(dto.diamanteId())
                    .orElse(null);
        }

        if (dto.familiaDestaqueId() != null) {
            familia = familiaRepository.findById(dto.familiaDestaqueId())
                    .orElse(null);
        }

        destaque.setDiamante(diamante);
        destaque.setFamiliaDestaque(familia);

        destaqueRepository.save(destaque);

        return toResponse(destaque);
    }

    public List<DestaqueTemporadaResponseDTO> listar() {
        return destaqueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DestaqueTemporadaResponseDTO buscarAtual() {
        DestaqueTemporada destaque = destaqueRepository
                .findTopByOrderByCriadoEmDesc()
                .orElseThrow(() -> new RuntimeException("Nenhum destaque encontrado"));

        return toResponse(destaque);
    }

    public DestaqueTemporadaResponseDTO buscarPorTemporada(String temporada) {
        DestaqueTemporada destaque = destaqueRepository
                .findByTemporada(temporada)
                .orElseThrow(() -> new RuntimeException("Temporada não encontrada: " + temporada));

        return toResponse(destaque);
    }

    @Transactional
    public DestaqueTemporadaResponseDTO atualizar(String id, DestaqueTemporadaRequestDTO dto) {

        DestaqueTemporada destaque =
                destaqueRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Destaque não encontrado"));

        destaque.setTemporada(dto.temporada());

        Personagem diamante = null;
        Familia familia = null;

        if (dto.diamanteId() != null) {
            diamante = personagemRepository.findById(dto.diamanteId())
                    .orElse(null);
        }

        if (dto.familiaDestaqueId() != null) {
            familia = familiaRepository.findById(dto.familiaDestaqueId())
                    .orElse(null);
        }

        destaque.setDiamante(diamante);
        destaque.setFamiliaDestaque(familia);

        destaqueRepository.save(destaque);

        return toResponse(destaque);
    }

    private DestaqueTemporadaResponseDTO toResponse(DestaqueTemporada destaque) {
        return new DestaqueTemporadaResponseDTO(
                destaque.getId(),
                destaque.getTemporada(),
                destaque.getDiamante() != null ? destaque.getDiamante().getId_personagens() : null,
                destaque.getDiamante() != null ? destaque.getDiamante().getNome() : null,
                destaque.getFamiliaDestaque() != null ? destaque.getFamiliaDestaque().getId() : null,
                destaque.getFamiliaDestaque() != null ? destaque.getFamiliaDestaque().getNome() : null,
                destaque.getCriadoEm()
        );
    }
}