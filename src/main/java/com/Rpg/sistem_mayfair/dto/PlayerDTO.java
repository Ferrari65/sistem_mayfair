package com.Rpg.sistem_mayfair.dto;

import com.Rpg.sistem_mayfair.dto.personagem.PersonagemResumoDTO;

import java.util.List;

public class PlayerDTO {

    private Long id;

    private String nome;

    private String telefoneUltimos4;

    private List<PersonagemResumoDTO> personagens;

    public PlayerDTO() {
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

    public List<PersonagemResumoDTO> getPersonagens() {
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

    public void setPersonagens(List<PersonagemResumoDTO> personagens) {
        this.personagens = personagens;
    }
}