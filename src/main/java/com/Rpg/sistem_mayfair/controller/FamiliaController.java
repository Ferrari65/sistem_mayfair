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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/familias")
@RequiredArgsConstructor
@Tag(name = "Famílias", description = "Gestão das linhagens nobres de Mayfair")
public class FamiliaController {

    private final FamiliaRepository familiaRepository;
    private final PersonagemRepository personagemRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma nova família")
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

    @GetMapping
    @Operation(summary = "Lista todas as famílias cadastradas")
    public List<Familia> listarFamilias() {
        return familiaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Familia buscarPorId(@PathVariable Long id) {

        return familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));
    }

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public void deletarFamilia(@PathVariable Long id) {

        Familia familia = familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));

        for (Personagem personagem : familia.getPersonagens()) {
            personagem.setFamilia(null);
        }

        personagemRepository.saveAll(familia.getPersonagens());

        familiaRepository.delete(familia);
    }
}