package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.dto.FamiliaRequestDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
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
}