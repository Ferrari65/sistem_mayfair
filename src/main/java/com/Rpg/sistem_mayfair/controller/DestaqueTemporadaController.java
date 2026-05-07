package com.Rpg.sistem_mayfair.controller;


import com.Rpg.sistem_mayfair.domain.DestaqueTemporadaRequestDTO;
import com.Rpg.sistem_mayfair.domain.DestaqueTemporadaResponseDTO;
import com.Rpg.sistem_mayfair.service.DestaqueTemporadaService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destaques")
public class DestaqueTemporadaController {

    private final DestaqueTemporadaService service;

    public DestaqueTemporadaController(
            DestaqueTemporadaService service
    ) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DestaqueTemporadaResponseDTO> criar(
            @RequestBody DestaqueTemporadaRequestDTO dto
    ) {

        return ResponseEntity.ok(
                service.criar(dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<DestaqueTemporadaResponseDTO>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );
    }

    @GetMapping("/atual")
    public ResponseEntity<DestaqueTemporadaResponseDTO> buscarAtual() {
        return ResponseEntity.ok(
                service.buscarAtual()
        );
    }

    @GetMapping("/{temporada}")
    public ResponseEntity<DestaqueTemporadaResponseDTO>
    buscarPorTemporada(
            @PathVariable String temporada
    ) {

        return ResponseEntity.ok(
                service.buscarPorTemporada(temporada)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DestaqueTemporadaResponseDTO> atualizar(
            @PathVariable String id,
            @RequestBody DestaqueTemporadaRequestDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }
}