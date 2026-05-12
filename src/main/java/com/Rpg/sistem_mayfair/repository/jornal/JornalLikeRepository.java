package com.Rpg.sistem_mayfair.repository.jornal;

import com.Rpg.sistem_mayfair.domain.jornal.JornalLike;
import com.Rpg.sistem_mayfair.domain.jornal.JornalPostagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JornalLikeRepository
        extends JpaRepository<JornalLike, Long> {

    boolean existsByIpAddressAndPostagem(
            String ipAddress,
            JornalPostagem postagem
    );
}