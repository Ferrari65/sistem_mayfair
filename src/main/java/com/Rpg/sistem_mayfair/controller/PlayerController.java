package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Player;
import com.Rpg.sistem_mayfair.dto.PlayerDTO;
import com.Rpg.sistem_mayfair.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    // =========================
    // POST - Criar player
    // =========================
    @PostMapping
    public ResponseEntity<Player> criar(@RequestBody PlayerDTO dto) {

        Player player = service.criar(dto);

        return ResponseEntity.ok(player);
    }

    // =========================
    // GET - Listar todos
    // =========================
    @GetMapping
    public ResponseEntity<List<Player>> listarTodos() {

        List<Player> players = service.listarTodos();

        return ResponseEntity.ok(players);
    }

    // =========================
    // GET - Buscar por ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Player> buscarPorId(@PathVariable Long id) {

        Player player = service.buscarPorId(id);

        return ResponseEntity.ok(player);
    }
}