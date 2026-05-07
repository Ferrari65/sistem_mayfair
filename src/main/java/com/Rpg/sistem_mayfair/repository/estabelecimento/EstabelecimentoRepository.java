package com.Rpg.sistem_mayfair.repository.estabelecimento;

import com.Rpg.sistem_mayfair.domain.estabelecimento.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstabelecimentoRepository
        extends JpaRepository<Estabelecimento, Long> {
}