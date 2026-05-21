package com.Rpg.sistem_mayfair.dto.estabelecimento;

import com.Rpg.sistem_mayfair.domain.Enum.TipoMovimentacaoEstabelecimento;
import lombok.Data;

@Data
public class RegistrarMovimentacaoDTO {


    private TipoMovimentacaoEstabelecimento tipo;
    private Integer impactoMoral;
    private String observacao;
}