package com.Rpg.sistem_mayfair.repository.estabelecimento;

import com.Rpg.sistem_mayfair.domain.estabelecimento.AmbienteEstabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmbienteEstabelecimentoRepository extends JpaRepository<AmbienteEstabelecimento, Long> {

    /*
     * BUSCAR AMBIENTES POR ESTABELECIMENTO
     */
    List<AmbienteEstabelecimento> findByEstabelecimentoId(Long estabelecimentoId);
}