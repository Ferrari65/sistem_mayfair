package com.Rpg.sistem_mayfair.dto.personagem;

import com.Rpg.sistem_mayfair.domain.Enum.Genero;
import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonagemDTO {
    private String name;
    private Integer age;
    private String title;
    private Integer prestige;
    private String description;
    private String imageUrl;

    private Long familyId;
    private String shape;
    private Genero genero;
    private StatusCivil statusCivil;
    private Long parceiroId;
}