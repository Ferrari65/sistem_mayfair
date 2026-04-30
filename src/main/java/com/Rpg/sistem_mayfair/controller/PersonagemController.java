package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.dto.PersonagemDTO;
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
    public Personagem criarPersonagem(@RequestBody PersonagemDTO dto) {

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

        return personagemRepository.save(personagem);
    }

    @GetMapping
    public List<Personagem> listarPersonagens() {
        return personagemRepository.findAll();
    }

    @GetMapping("/{id}")
    public Personagem buscarPorId(@PathVariable Integer id) {
        return personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));
    }

    @PutMapping("/{id}")
    public Personagem atualizarPersonagem(
            @PathVariable Integer id,
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

        return personagemRepository.save(personagem);
    }

    @DeleteMapping("/{id}")
    public void deletarPersonagem(@PathVariable Integer id) {
        personagemRepository.deleteById(id);
    }
}