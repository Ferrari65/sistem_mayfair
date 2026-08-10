package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.EventoCalendario;
import com.Rpg.sistem_mayfair.repository.EventoCalendarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoCalendarioService {

    private final EventoCalendarioRepository repository;

    public EventoCalendarioService(EventoCalendarioRepository repository) {
        this.repository = repository;
    }

    // SALVAR
    public EventoCalendario salvar(EventoCalendario evento) {
        return repository.save(evento);
    }

    // LISTAR TODOS
    public List<EventoCalendario> listar() {
        return repository.findAll();
    }

    // BUSCAR POR ID
    public EventoCalendario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Evento não encontrado com o ID: " + id)
                );
    }

    // ATUALIZAR
    public EventoCalendario atualizar(Long id, EventoCalendario evento) {

        EventoCalendario eventoExistente = buscarPorId(id);

        eventoExistente.setTitulo(evento.getTitulo());
        eventoExistente.setData(evento.getData());
        eventoExistente.setLocal(evento.getLocal());
        eventoExistente.setAnfitriao(evento.getAnfitriao());
        eventoExistente.setDressCode(evento.getDressCode());

        return repository.save(eventoExistente);
    }

    // EXCLUIR
    public void excluir(Long id) {

        EventoCalendario evento = buscarPorId(id);

        repository.delete(evento);
    }
}
