package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Enum.JornalReacaoTipo;
import com.Rpg.sistem_mayfair.dto.jornal.JornalPostagemRequestDTO;
import com.Rpg.sistem_mayfair.dto.jornal.JornalPostagemResponseDTO;
import com.Rpg.sistem_mayfair.dto.jornal.JornalReacaoResponseDTO;
import com.Rpg.sistem_mayfair.service.JornalPostagemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jornal")
@RequiredArgsConstructor
public class JornalPostagemController {

    private final JornalPostagemService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public JornalPostagemResponseDTO criar(
            @RequestBody JornalPostagemRequestDTO dto
    ) {
        return service.criar(dto);
    }

    @GetMapping
    public List<JornalPostagemResponseDTO> listar() {
        return service.listar();
    }

    @PostMapping("/{id}/like")
    public void like(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        service.like(id, request.getRemoteAddr());
    }

    @PostMapping("/{id}/reacao")
    public void reagir(
            @PathVariable Long id,
            @RequestParam JornalReacaoTipo tipo,
            HttpServletRequest request
    ) {
        service.reagir(id, tipo, request.getRemoteAddr());
    }

    @GetMapping("/{id}")
    public JornalPostagemResponseDTO detalhar(
            @PathVariable Long id
    ) {
        return service.detalhar(id);
    }

    @GetMapping("/{id}/reacoes")
    public List<JornalReacaoResponseDTO> listarReacoes(@PathVariable Long id) {
        return service.listarReacoes(id);
    }
}