package com.Rpg.sistem_mayfair.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class PersonagemDTO {

    private String nome;
    private Integer idade;
    private String titulo;
    private Integer prestigio;
    private String descricao;
    private String foto;
    private Long idFamilia;
}