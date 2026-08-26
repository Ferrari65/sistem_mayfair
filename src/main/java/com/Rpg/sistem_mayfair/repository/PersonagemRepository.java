package com.Rpg.sistem_mayfair.repository;

import com.Rpg.sistem_mayfair.domain.Personagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonagemRepository
        extends JpaRepository<Personagem, Long> {

    List<Personagem> findByParceiro(Personagem parceiro);
}