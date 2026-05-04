package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.dto.PersonagemDTO;
import com.Rpg.sistem_mayfair.dto.PersonagemResponseDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagens")
@RequiredArgsConstructor
public class PersonagemController {

    private final PersonagemRepository personagemRepository;
    private final FamiliaRepository familiaRepository;

    // =========================================
    // CRIAR PERSONAGEM
    // =========================================
    @PostMapping
    public PersonagemResponseDTO criarPersonagem(@RequestBody PersonagemDTO dto) {

        Familia familia = null;

        if (dto.getFamily() != null && !dto.getFamily().isBlank()) {
            familia = familiaRepository.findByNome(dto.getFamily())
                    .orElse(null);
        }

        Personagem personagem = new Personagem();
        personagem.setNome(dto.getName());
        personagem.setIdade(dto.getAge());
        personagem.setTitulo(dto.getTitle());
        personagem.setPrestigio(dto.getPrestige() != null ? dto.getPrestige() : 20);
        personagem.setDescricao(dto.getNotes());
        personagem.setFoto(dto.getPhotoUrl());
        personagem.setFamilia(familia);

        return new PersonagemResponseDTO(
                personagemRepository.save(personagem)
        );
    }

    // =========================================
    // LISTAR
    // =========================================
    @GetMapping
    public List<PersonagemResponseDTO> listarPersonagens() {
        return personagemRepository.findAll()
                .stream()
                .map(PersonagemResponseDTO::new)
                .toList();
    }

    // =========================================
    // BUSCAR POR ID
    // =========================================
    @GetMapping("/{id}")
    public PersonagemResponseDTO buscarPorId(@PathVariable Long id) {

        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        return new PersonagemResponseDTO(personagem);
    }

    // =========================================
    // ATUALIZAR
    // =========================================
    @PutMapping("/{id}")
    public PersonagemResponseDTO atualizarPersonagem(
            @PathVariable Long id,
            @RequestBody PersonagemDTO dto
    ) {

        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        Familia familia = null;

        if (dto.getFamily() != null && !dto.getFamily().isBlank()) {
            familia = familiaRepository.findByNome(dto.getFamily())
                    .orElse(null);
        }

        personagem.setNome(dto.getName());
        personagem.setIdade(dto.getAge());
        personagem.setTitulo(dto.getTitle());
        personagem.setPrestigio(dto.getPrestige() != null ? dto.getPrestige() : 20);
        personagem.setDescricao(dto.getNotes());
        personagem.setFoto(dto.getPhotoUrl());
        personagem.setFamilia(familia);

        return new PersonagemResponseDTO(
                personagemRepository.save(personagem)
        );
    }

    // =========================================
    // DELETAR
    // =========================================
    @DeleteMapping("/{id}")
    public void deletarPersonagem(@PathVariable Long id) {
        personagemRepository.deleteById(id);
    }
}