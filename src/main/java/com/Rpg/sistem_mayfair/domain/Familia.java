package com.Rpg.sistem_mayfair.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table (name = "familias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Familia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String titulo;
    private String dilema;
    private String photoUrl;
    private String matriarca;
    private String patriarca;

    @JsonBackReference
    @OneToMany(mappedBy = "familia")
    private List<Personagem> personagens;
}
