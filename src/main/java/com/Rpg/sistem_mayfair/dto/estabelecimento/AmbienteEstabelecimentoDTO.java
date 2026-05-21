package com.Rpg.sistem_mayfair.dto.estabelecimento;

import lombok.Data;

import java.util.List;

@Data
public class AmbienteEstabelecimentoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String tipo;
    private List<String> fotos;

}