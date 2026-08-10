package com.Rpg.sistem_mayfair.domain.jornal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jornal_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JornalLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "postagem_id")
    private com.Rpg.sistem_mayfair.domain.JornalPostagem postagem;
}
