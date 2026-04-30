package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.HistoricoPrestigio;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.dto.HistoricoPrestigioDTO;
import com.Rpg.sistem_mayfair.repository.HistoricoPrestigioRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
public class HistoricoPrestigioController {

    private final HistoricoPrestigioRepository historicoRepository;
    private final PersonagemRepository personagemRepository;

    @PostMapping
    public HistoricoPrestigio criarHistorico(
            @RequestBody HistoricoPrestigioDTO dto
    ) {

        Personagem personagem = personagemRepository.findById(dto.getIdPersonagem())
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        personagem.setPrestigio(
                personagem.getPrestigio() + dto.getPontos()
        );

        personagemRepository.save(personagem);

        HistoricoPrestigio historico = new HistoricoPrestigio();

        historico.setDescricao(dto.getDescricao());
        historico.setPontos(dto.getPontos());

        historico.setCreatedAt(LocalDateTime.now());

        historico.setPersonagem(personagem);

        return historicoRepository.save(historico);
    }

    @GetMapping
    public List<HistoricoPrestigio> listarHistorico() {
        return historicoRepository.findAll();
    }

    @GetMapping("/{id}")
    public HistoricoPrestigio buscarPorId(
            @PathVariable Integer id
    ) {

        return historicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado"));
    }

    @PutMapping("/{id}")
    public HistoricoPrestigio atualizarHistorico(
            @PathVariable Integer id,
            @RequestBody HistoricoPrestigioDTO dto
    ) {

        HistoricoPrestigio historico = historicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado"));

        Personagem personagem = personagemRepository.findById(dto.getIdPersonagem())
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        historico.setDescricao(dto.getDescricao());
        historico.setPontos(dto.getPontos());

        historico.setPersonagem(personagem);

        return historicoRepository.save(historico);
    }

    @DeleteMapping("/{id}")
    public void deletarHistorico(
            @PathVariable Integer id
    ) {

        historicoRepository.deleteById(id);
    }
}