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
    // Criar player
    // =========================
    public Player criar(PlayerDTO dto) {

        Player player = new Player();

        player.setNome(dto.getNome());
        player.setTelefoneUltimos4(dto.getTelefoneUltimos4());

        return repository.save(player);
    }

    // =========================
    // Listar todos
    // =========================
    public List<Player> listarTodos() {
        return repository.findAll();
    }

    // =========================
    // Buscar por ID
    // =========================
    public Player buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));
    }

    // =========================
    // UPDATE
    // =========================
    public Player atualizar(Long id, PlayerDTO dto) {

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

        return repository.save(player);
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
}