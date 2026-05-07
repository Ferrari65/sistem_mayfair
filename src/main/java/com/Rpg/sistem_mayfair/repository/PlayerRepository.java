package com.Rpg.sistem_mayfair.repository;

import com.Rpg.sistem_mayfair.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}