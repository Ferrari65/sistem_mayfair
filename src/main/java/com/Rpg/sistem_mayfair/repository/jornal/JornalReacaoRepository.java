package com.Rpg.sistem_mayfair.repository.jornal;

import com.Rpg.sistem_mayfair.domain.Enum.JornalReacaoTipo;
import com.Rpg.sistem_mayfair.domain.JornalPostagem;
import com.Rpg.sistem_mayfair.domain.jornal.JornalReacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JornalReacaoRepository
        extends JpaRepository<JornalReacao, Long> {

    boolean existsByIpAddressAndPostagemAndTipo(
            String ipAddress,
            JornalPostagem postagem,
            JornalReacaoTipo tipo
    );

    Integer countByPostagemAndTipo(
            JornalPostagem postagem,
            JornalReacaoTipo tipo
    );
}