package com.Rpg.sistem_mayfair.repository;

import com.Rpg.sistem_mayfair.domain.EventoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoCalendarioRepository
    extends JpaRepository<EventoCalendario, Long>
{
}