package com.Rpg.sistem_mayfair.domain.estabelecimento;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FotoAmbiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    @ManyToOne
    private AmbienteEstabelecimento ambiente;


}