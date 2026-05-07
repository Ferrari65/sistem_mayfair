package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.dto.FamiliaRequestDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/familias")
@RequiredArgsConstructor
@Tag(name = "Famílias", description = "Gestão das linhagens nobres de Mayfair")
public class FamiliaController {

    private final FamiliaRepository familiaRepository;
    private final PersonagemRepository personagemRepository;

    // ================= CREATE =================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Familia criarFamilia(@RequestBody @Valid FamiliaRequestDTO data) {

        Familia familia = new Familia();

        familia.setNome(data.nome());
        familia.setTitulo(data.titulo());
        familia.setDilema(data.dilema());
        familia.setPhotoUrl(data.photoUrl());
        familia.setMatriarca(data.matriarca());
        familia.setPatriarca(data.patriarca());

        return familiaRepository.save(familia);
    }

    // ================= LIST =================
    @GetMapping
    public List<Familia> listarFamilias() {
        return familiaRepository.findAll();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public Familia buscarPorId(@PathVariable Long id) {
        return familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Familia atualizarFamilia(
            @PathVariable Long id,
            @RequestBody FamiliaRequestDTO dto
    ) {

        Familia familia = familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));

        familia.setNome(dto.nome());
        familia.setTitulo(dto.titulo());
        familia.setDilema(dto.dilema());
        familia.setPhotoUrl(dto.photoUrl());
        familia.setMatriarca(dto.matriarca());
        familia.setPatriarca(dto.patriarca());

        return familiaRepository.save(familia);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarFamilia(@PathVariable Long id) {

        Familia familia = familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));

        for (var personagem : familia.getPersonagens()) {
            personagem.setFamilia(null);
        }

        personagemRepository.saveAll(familia.getPersonagens());

        familiaRepository.delete(familia);
    }
}