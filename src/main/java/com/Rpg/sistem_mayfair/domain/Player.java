package com.Rpg.sistem_mayfair.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String telefoneUltimos4;

    @OneToMany(mappedBy = "player")
    private List<Personagem> personagens;
}
