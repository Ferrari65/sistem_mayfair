package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Player;
import com.Rpg.sistem_mayfair.dto.PlayerDTO;
import com.Rpg.sistem_mayfair.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> criar(@RequestBody PlayerDTO dto) {

        PlayerDTO player = service.criar(dto);

        return ResponseEntity.ok(player);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> listarTodos() {

        List<PlayerDTO> players = service.listarTodos();

        return ResponseEntity.ok(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> buscarPorId(@PathVariable Long id) {

        PlayerDTO player = service.buscarPorId(id);

        return ResponseEntity.ok(player);
    }

    @PutMapping("/{id}")
    public PlayerDTO atualizar(
            @PathVariable Long id,
            @RequestBody PlayerDTO dto
    ) {

        return service.atualizar(id, dto);
    }
    // =========================
    // DELETE
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long id
    ) {

        service.deletar(id);
    }
}