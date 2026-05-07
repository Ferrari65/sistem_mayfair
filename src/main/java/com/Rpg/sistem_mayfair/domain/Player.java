package com.Rpg.sistem_mayfair.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private List<Personagem> personagens;

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefoneUltimos4() {
        return telefoneUltimos4;
    }

    public List<Personagem> getPersonagens() {
        return personagens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefoneUltimos4(String telefoneUltimos4) {
        this.telefoneUltimos4 = telefoneUltimos4;
    }

    public void setPersonagens(List<Personagem> personagens) {
        this.personagens = personagens;
    }
}