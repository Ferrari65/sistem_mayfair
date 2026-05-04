package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.HistoricoPrestigio;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.repository.HistoricoPrestigioRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrestigioService {

    private final PersonagemRepository personagemRepository;
    private final HistoricoPrestigioRepository historicoRepository;

    // =========================================
    // APLICAR EVENTO (fluxo principal)
    // =========================================
    public Personagem aplicarEvento(Long personagemId, String descricao, int delta) {

        Personagem personagem = personagemRepository.findById(personagemId)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        int novoPrestigio = personagem.getPrestigio() + delta;

        // clamp 0–50
        if (novoPrestigio > 50) novoPrestigio = 50;
        if (novoPrestigio < 0) novoPrestigio = 0;

        personagem.setPrestigio(novoPrestigio);

        // salvar histórico (log do RPG)
        HistoricoPrestigio historico = new HistoricoPrestigio();
        historico.setDescricao(descricao);
        historico.setPontos(delta);
        historico.setPersonagem(personagem);
        historico.setCreatedAt(LocalDateTime.now());

        historicoRepository.save(historico);

        return personagemRepository.save(personagem);
    }

    // =========================================
    // RECONSTRUIR PRESTÍGIO (SOURCE OF TRUTH)
    // =========================================
    public Personagem recalcularPrestigio(Long personagemId) {

        Personagem personagem = personagemRepository.findById(personagemId)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        int base = 20;

        int total = personagem.getHistoricoPrestigio()
                .stream()
                .mapToInt(HistoricoPrestigio::getPontos)
                .sum();

        int novoPrestigio = base + total;

        // clamp 0–50
        if (novoPrestigio > 50) novoPrestigio = 50;
        if (novoPrestigio < 0) novoPrestigio = 0;

        personagem.setPrestigio(novoPrestigio);

        return personagemRepository.save(personagem);
    }
}