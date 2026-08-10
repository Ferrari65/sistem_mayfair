package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.EventoCalendario;
import com.Rpg.sistem_mayfair.service.EventoCalendarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoCalendarioController {

    private final EventoCalendarioService service;

    public EventoCalendarioController(EventoCalendarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventoCalendario> cadastrar(
            @RequestBody EventoCalendario evento) {

        EventoCalendario novoEvento = service.salvar(evento);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novoEvento);
    }

    @GetMapping
    public ResponseEntity<List<EventoCalendario>> listar() {

        List<EventoCalendario> eventos = service.listar();

        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoCalendario> buscarPorId(
            @PathVariable Long id) {

        EventoCalendario evento = service.buscarPorId(id);

        return ResponseEntity.ok(evento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoCalendario> atualizar(
            @PathVariable Long id,
            @RequestBody EventoCalendario evento) {

        EventoCalendario eventoAtualizado =
                service.atualizar(id, evento);

        return ResponseEntity.ok(eventoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}