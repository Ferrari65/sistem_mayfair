package com.Rpg.sistem_mayfair.repository;

import com.Rpg.sistem_mayfair.domain.DestaqueTemporada;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DestaqueTemporadaRepository
        extends JpaRepository<DestaqueTemporada, String> {

    Optional<DestaqueTemporada>
    findTopByOrderByCriadoEmDesc();

    Optional<DestaqueTemporada>
    findByTemporada(String temporada);

    boolean existsByTemporada(String temporada);
}