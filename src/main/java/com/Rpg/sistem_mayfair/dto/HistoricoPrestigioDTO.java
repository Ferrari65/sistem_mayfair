package com.Rpg.sistem_mayfair.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoPrestigioDTO {
    private Long idPersonagem;
    private String descricao;
    private Integer pontos;
}