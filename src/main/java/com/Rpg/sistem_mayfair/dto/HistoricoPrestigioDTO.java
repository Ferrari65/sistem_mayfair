package com.Rpg.sistem_mayfair.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoPrestigioDTO {

    private Long idHistorico;
    private Long idPersonagem;
    private String descricao;
    private Integer pontos;
    private OffsetDateTime createdAt;
}