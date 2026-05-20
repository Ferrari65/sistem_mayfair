package com.Rpg.sistem_mayfair.dto.estabelecimento;

import java.time.LocalDateTime;
import java.util.Map;

public record EstatisticasEstabelecimentoDTO(

        Long totalMovimentacoes,
        Map<String, Long> movimentacoesPorTipo,
        Integer impactoMoralTotal,
        LocalDateTime ultimaMovimentacao

) {
}
