package com.Rpg.sistem_mayfair.dto.estabelecimento;

import com.Rpg.sistem_mayfair.domain.Enum.TipoMovimentacaoEstabelecimento;
import lombok.Data;

@Data
public class RegistrarMovimentacaoDTO {


    private TipoMovimentacaoEstabelecimento tipo;

    /*
     * IMPACTO NA MORAL
     * Pode ser positivo ou negativo
     */
    private Integer impactoMoral;

    /*
     * OBSERVAÇÃO / CONTEXTO
     */
    private String observacao;
}