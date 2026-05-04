package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.dto.EventoPrestigioDTO;
import com.Rpg.sistem_mayfair.dto.PersonagemDTO;
import com.Rpg.sistem_mayfair.dto.PersonagemResponseDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.service.PrestigioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagens")
@RequiredArgsConstructor
public class PersonagemController {

    private final PersonagemRepository personagemRepository;
    private final FamiliaRepository familiaRepository;
    private final PrestigioService prestigioService;

    // =========================================
    // CRIAR PERSONAGEM (OK)
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
        personagem.setDescricao(dto.getDescription());

        // 🔥 BLINDADO (não salva null sem necessidade)
        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            personagem.setImageUrl(dto.getImageUrl().trim());
        }

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
    // ATUALIZAR (🔥 BLINDADO CONTRA PERDA DE DADOS)
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

        // 🔥 UPDATE SEGURO (NUNCA SOBRESCREVE COM NULL)

        if (dto.getName() != null) {
            personagem.setNome(dto.getName());
        }

        if (dto.getAge() != null) {
            personagem.setIdade(dto.getAge());
        }

        if (dto.getTitle() != null) {
            personagem.setTitulo(dto.getTitle());
        }

        if (dto.getPrestige() != null) {
            personagem.setPrestigio(dto.getPrestige());
        }

        if (dto.getDescription() != null) {
            personagem.setDescricao(dto.getDescription());
        }

        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            personagem.setImageUrl(dto.getImageUrl().trim());
        }

        if (familia != null) {
            personagem.setFamilia(familia);
        }

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

    // =========================================
    // EVENTOS
    // =========================================
    @PostMapping("/{id}/eventos")
    public PersonagemResponseDTO adicionarEvento(
            @PathVariable Long id,
            @RequestBody EventoPrestigioDTO dto
    ) {

        Personagem atualizado = prestigioService.aplicarEvento(
                id,
                dto.getReason(),
                dto.getDelta()
        );

        return new PersonagemResponseDTO(atualizado);
    }

    @PostMapping("/{id}/recalcular-prestigio")
    public PersonagemResponseDTO recalcular(@PathVariable Long id) {

        Personagem personagem = prestigioService.recalcularPrestigio(id);

        return new PersonagemResponseDTO(personagem);
    }
}