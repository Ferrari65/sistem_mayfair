package com.Rpg.sistem_mayfair.repository.estabelecimento;

import com.Rpg.sistem_mayfair.domain.estabelecimento.MovimentacaoEstabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstabelecimentoRepository
        extends JpaRepository<MovimentacaoEstabelecimento, Long> {

    List<MovimentacaoEstabelecimento>
    findByEstabelecimentoIdOrderByDataMovimentacaoDesc(Long estabelecimentoId);
}