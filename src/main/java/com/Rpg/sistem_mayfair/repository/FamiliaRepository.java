package com.Rpg.sistem_mayfair.repository;

import com.Rpg.sistem_mayfair.domain.Familia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamiliaRepository extends JpaRepository<Familia, Long> {

    Optional<Familia> findByNome(String nome);
}