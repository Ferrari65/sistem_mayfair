package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.Player;
import com.Rpg.sistem_mayfair.dto.PlayerDTO;
import com.Rpg.sistem_mayfair.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    // =========================
    // CRIAR PLAYER
    // =========================
    public PlayerDTO criar(PlayerDTO dto) {

        Player player = new Player();

        player.setNome(dto.getNome());
        player.setTelefoneUltimos4(dto.getTelefoneUltimos4());

        Player salvo = repository.save(player);

        return toDTO(salvo);
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<PlayerDTO> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public PlayerDTO buscarPorId(Long id) {

        Player player = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Player não encontrado")
                );

        return toDTO(player);
    }

    // =========================
    // UPDATE
    // =========================
    public PlayerDTO atualizar(Long id, PlayerDTO dto) {

        Player player = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Player não encontrado")
                );

        // Atualiza apenas se vier valor
        if (dto.getNome() != null) {
            player.setNome(dto.getNome());
        }

        if (dto.getTelefoneUltimos4() != null) {
            player.setTelefoneUltimos4(dto.getTelefoneUltimos4());
        }

        Player atualizado = repository.save(player);

        return toDTO(atualizado);
    }

    // =========================
    // DELETE
    // =========================
    @Transactional
    public void deletar(Long id) {

        Player player = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Player não encontrado")
                );

        // =========================
        // REMOVE VÍNCULO DOS PERSONAGENS
        // =========================
        for (Personagem personagem : player.getPersonagens()) {

            personagem.setPlayer(null);
        }

        // =========================
        // DELETA PLAYER
        // =========================
        repository.delete(player);
    }

    // =========================
    // CONVERTER ENTITY -> DTO
    // =========================
    private PlayerDTO toDTO(Player player) {

        PlayerDTO dto = new PlayerDTO();

        dto.setNome(player.getNome());
        dto.setTelefoneUltimos4(player.getTelefoneUltimos4());

        return dto;
    }
}