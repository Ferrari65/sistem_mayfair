package com.Rpg.sistem_mayfair.dto.estabelecimento;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
public class EstabelecimentoDTO {

    // Adicionado para que o Front-end saiba qual ID buscar nos detalhes
    private Long id;

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
    private Long proprietarioId;
    private List<Long> funcionariosIds;

    /*
     * MÍDIA (Sincronizado com o Front-end)
     */
    private List<FotoEstabelecimentoDTO> fotos;
    private List<AmbienteEstabelecimentoDTO> ambientes;
}