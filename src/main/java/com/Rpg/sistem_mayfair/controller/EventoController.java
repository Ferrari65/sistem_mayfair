package com.Rpg.sistem_mayfair.controller;


import com.Rpg.sistem_mayfair.dto.EventoDTO;
import com.Rpg.sistem_mayfair.service.EventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    /*
     * LISTAR TODOS
     */
    @GetMapping
    public ResponseEntity<List<EventoDTO>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    /*
     * BUSCAR POR ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<EventoDTO> criar(
            @RequestBody EventoDTO dto
    ) {

        return ResponseEntity.ok(
                service.criar(dto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody EventoDTO dto
    ) {

        return ResponseEntity.ok(
                service.atualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}