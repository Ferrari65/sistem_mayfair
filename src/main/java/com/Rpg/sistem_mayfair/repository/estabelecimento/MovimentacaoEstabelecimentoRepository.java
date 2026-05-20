package com.Rpg.sistem_mayfair.repository.estabelecimento;

import com.Rpg.sistem_mayfair.domain.estabelecimento.MovimentacaoEstabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimentacaoEstabelecimentoRepository
        extends JpaRepository<MovimentacaoEstabelecimento, Long> {

    List<MovimentacaoEstabelecimento>
    findByEstabelecimentoIdOrderByDataMovimentacaoDesc(Long estabelecimentoId);

    @Query("""
    SELECT m.tipo, COUNT(m)
    FROM MovimentacaoEstabelecimento m
    WHERE m.estabelecimento.id = :estabelecimentoId
    GROUP BY m.tipo
""")
    List<Object[]> contarMovimentacoesPorTipo(Long estabelecimentoId);

    @Query("""
    SELECT COALESCE(SUM(m.impactoMoral), 0)
    FROM MovimentacaoEstabelecimento m
    WHERE m.estabelecimento.id = :estabelecimentoId
""")
    Integer somarImpactoMoral(Long estabelecimentoId);

    MovimentacaoEstabelecimento
    findTopByEstabelecimentoIdOrderByDataMovimentacaoDesc(Long estabelecimentoId);
}