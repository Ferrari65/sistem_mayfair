package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.AtribuicaoLeitura;
import com.Rpg.sistem_mayfair.domain.Evento;
import com.Rpg.sistem_mayfair.domain.Player;
import com.Rpg.sistem_mayfair.dto.atribuirLeituraCena.AtribuicaoLeituraRequest;
import com.Rpg.sistem_mayfair.dto.atribuirLeituraCena.AtribuicaoLeituraResponse;
import com.Rpg.sistem_mayfair.repository.AtribuicaoLeituraRepository;
import com.Rpg.sistem_mayfair.repository.EventoRepository;
import com.Rpg.sistem_mayfair.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtribuicaoLeituraService {

    private final AtribuicaoLeituraRepository atribuicaoRepository;
    private final EventoRepository eventoRepository;
    private final PlayerRepository playerRepository;

    public AtribuicaoLeituraService(
            AtribuicaoLeituraRepository atribuicaoRepository,
            EventoRepository eventoRepository,
            PlayerRepository playerRepository
    ) {
        this.atribuicaoRepository = atribuicaoRepository;
        this.eventoRepository = eventoRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public AtribuicaoLeituraResponse atribuir(
            AtribuicaoLeituraRequest request
    ) {

        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evento não encontrado: " + request.getEventoId()
                        )
                );

        Player player = playerRepository.findById(request.getPlayerId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Player não encontrado: " + request.getPlayerId()
                        )
                );

        if (atribuicaoRepository.existsByEventoId(evento.getId())) {
            throw new RuntimeException(
                    "Este evento já possui um player responsável pela leitura."
            );
        }

        AtribuicaoLeitura atribuicao =
                new AtribuicaoLeitura(evento, player);

        atribuicao = atribuicaoRepository.save(atribuicao);

        return converter(atribuicao);
    }

    @Transactional(readOnly = true)
    public AtribuicaoLeituraResponse buscarPorEvento(Long eventoId) {

        AtribuicaoLeitura atribuicao =
                atribuicaoRepository.findByEventoId(eventoId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Este evento não possui atribuição de leitura."
                                )
                        );

        return converter(atribuicao);
    }

    @Transactional(readOnly = true)
    public List<AtribuicaoLeituraResponse> buscarPorPlayer(Long playerId) {

        if (!playerRepository.existsById(playerId)) {
            throw new RuntimeException(
                    "Player não encontrado: " + playerId
            );
        }

        return atribuicaoRepository.findByPlayerId(playerId)
                .stream()
                .map(this::converter)
                .toList();
    }

    @Transactional
    public void remover(Long eventoId) {

        AtribuicaoLeitura atribuicao =
                atribuicaoRepository.findByEventoId(eventoId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Este evento não possui atribuição de leitura."
                                )
                        );

        atribuicaoRepository.delete(atribuicao);
    }

    private AtribuicaoLeituraResponse converter(
            AtribuicaoLeitura atribuicao
    ) {

        return new AtribuicaoLeituraResponse(
                atribuicao.getId(),
                atribuicao.getEvento().getId(),
                atribuicao.getEvento().getTitulo(),
                atribuicao.getPlayer().getId(),
                atribuicao.getPlayer().getNome()
        );
    }
}