package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.dto.atribuirLeituraCena.AtribuicaoLeituraRequest;
import com.Rpg.sistem_mayfair.dto.atribuirLeituraCena.AtribuicaoLeituraResponse;
import com.Rpg.sistem_mayfair.service.AtribuicaoLeituraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atribuicoes-leitura")
public class AtribuicaoLeituraController {

    private final AtribuicaoLeituraService service;

    public AtribuicaoLeituraController(
            AtribuicaoLeituraService service
    ) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AtribuicaoLeituraResponse> atribuir(
            @RequestBody AtribuicaoLeituraRequest request
    ) {

        AtribuicaoLeituraResponse response =
                service.atribuir(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<AtribuicaoLeituraResponse> buscarPorEvento(
            @PathVariable Long eventoId
    ) {

        return ResponseEntity.ok(
                service.buscarPorEvento(eventoId)
        );
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<AtribuicaoLeituraResponse>> buscarPorPlayer(
            @PathVariable Long playerId
    ) {

        return ResponseEntity.ok(
                service.buscarPorPlayer(playerId)
        );
    }

    @DeleteMapping("/evento/{eventoId}")
    public ResponseEntity<Void> remover(
            @PathVariable Long eventoId
    ) {

        service.remover(eventoId);

        return ResponseEntity.noContent().build();
    }
}