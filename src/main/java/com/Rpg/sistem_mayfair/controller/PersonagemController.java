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

    @PostMapping
    public PersonagemResponseDTO criarPersonagem(@RequestBody PersonagemDTO dto) {

        Familia familia = familiaRepository.findById(dto.getIdFamilia())
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));

        Personagem personagem = new Personagem();

        personagem.setNome(dto.getNome());
        personagem.setIdade(dto.getIdade());
        personagem.setTitulo(dto.getTitulo());
        personagem.setPrestigio(dto.getPrestigio());
        personagem.setDescricao(dto.getDescricao());
        personagem.setFoto(dto.getFoto());

        personagem.setFamilia(familia);

        // O campo id_personagens e createdAt são gerados automaticamente pela Entity
        Personagem salvo = personagemRepository.save(personagem);

        return new PersonagemResponseDTO(salvo);
    }

    @GetMapping
    public List<PersonagemResponseDTO> listarPersonagens() {

        return personagemRepository.findAll()
                .stream()
                .map(PersonagemResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public PersonagemResponseDTO buscarPorId(@PathVariable Long id) {

        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        return new PersonagemResponseDTO(personagem);
    }

    @PutMapping("/{id}")
    public PersonagemResponseDTO atualizarPersonagem(
            @PathVariable Long id,
            @RequestBody PersonagemDTO dto
    ) {

        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        Familia familia = familiaRepository.findById(dto.getIdFamilia())
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));

        personagem.setNome(dto.getNome());
        personagem.setIdade(dto.getIdade());
        personagem.setTitulo(dto.getTitulo());
        personagem.setPrestigio(dto.getPrestigio());
        personagem.setDescricao(dto.getDescricao());
        personagem.setFoto(dto.getFoto());

        personagem.setFamilia(familia);

        Personagem atualizado = personagemRepository.save(personagem);

        return new PersonagemResponseDTO(atualizado);
    }

    @DeleteMapping("/{id}")
    public void deletarPersonagem(@PathVariable Long id) {

        personagemRepository.deleteById(id);
    }
}