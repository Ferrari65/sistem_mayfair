package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamiliaService {

    private final FamiliaRepository familiaRepository;
    private final PersonagemRepository personagemRepository;

    public void deletar(Long id) {

        Familia familia = familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Família não encontrada"));

        for (Personagem personagem : familia.getPersonagens()) {
            personagem.setFamilia(null);
        }

        personagemRepository.saveAll(familia.getPersonagens());

        familiaRepository.delete(familia);
    }
}
