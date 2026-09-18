package com.Rpg.sistem_mayfair.repository;


import com.Rpg.sistem_mayfair.domain.AtribuicaoLeitura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtribuicaoLeituraRepository
        extends JpaRepository<AtribuicaoLeitura, Long> {

    Optional<AtribuicaoLeitura> findByEventoId(Long eventoId);

    List<AtribuicaoLeitura> findByPlayerId(Long playerId);

    boolean existsByEventoId(Long eventoId);
}
