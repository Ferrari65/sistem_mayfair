package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.Evento;
import com.Rpg.sistem_mayfair.dto.EventoDTO;
import com.Rpg.sistem_mayfair.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repository;

    public EventoService(EventoRepository repository) {
        this.repository = repository;
    }

    public List<EventoDTO> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public EventoDTO buscarPorId(Long id) {

        Evento evento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        return toDTO(evento);
    }

    public EventoDTO criar(EventoDTO dto) {

        Evento evento = new Evento();

        evento.setTitulo(dto.titulo());
        evento.setDescricao(dto.descricao());
        evento.setFinalizado(dto.finalizado());

        repository.save(evento);

        return toDTO(evento);
    }

    public EventoDTO atualizar(Long id, EventoDTO dto) {

        Evento evento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        evento.setTitulo(dto.titulo());
        evento.setDescricao(dto.descricao());
        evento.setFinalizado(dto.finalizado());

        repository.save(evento);

        return toDTO(evento);
    }

    public void deletar(Long id) {

        Evento evento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        repository.delete(evento);
    }

    private EventoDTO toDTO(Evento evento) {

        return new EventoDTO(
                evento.getId(),
                evento.getTitulo(),
                evento.getDescricao(),
                evento.getFinalizado()
        );
    }
}