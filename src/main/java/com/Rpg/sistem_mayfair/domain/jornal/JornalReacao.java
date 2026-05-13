package com.Rpg.sistem_mayfair.domain.jornal;

import com.Rpg.sistem_mayfair.domain.Enum.JornalReacaoTipo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jornal_reacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JornalReacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private JornalReacaoTipo tipo;

    @ManyToOne
    @JoinColumn(name = "postagem_id")
    private com.Rpg.sistem_mayfair.domain.jornal.JornalPostagem postagem;
}