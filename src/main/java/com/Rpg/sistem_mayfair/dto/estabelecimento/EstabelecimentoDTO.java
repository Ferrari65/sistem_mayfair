package com.Rpg.sistem_mayfair.dto.estabelecimento;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class EstabelecimentoDTO {

    /*
     * INFORMAÇÕES BÁSICAS
     */

    private String nomeLocal;

    private String descricao;

    /*
     * SISTEMA DO ESTABELECIMENTO
     */

    private Integer moral;

    private Double dinheiro;

    /*
     * HORÁRIOS
     */

    private LocalTime horarioAbertura;

    private LocalTime horarioFechamento;

    /*
     * RELACIONAMENTOS
     */

    // ID do personagem proprietário
    private Long proprietarioId;

    // IDs dos funcionários
    private List<Long> funcionariosIds;
    private FotoEstabelecimentoDTO foto;
}