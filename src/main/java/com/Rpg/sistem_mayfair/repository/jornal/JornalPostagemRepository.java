package com.Rpg.sistem_mayfair.repository.jornal;

import com.Rpg.sistem_mayfair.domain.jornal.JornalPostagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JornalPostagemRepository
        extends JpaRepository<JornalPostagem, Long> {
}