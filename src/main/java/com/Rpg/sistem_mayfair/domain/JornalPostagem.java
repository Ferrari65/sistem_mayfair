package com.Rpg.sistem_mayfair.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jornal_postagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JornalPostagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String noticia;

    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Integer likes = 0;


    // ============================================================
    // PERSONAGENS DO JORNAL
    // ============================================================

    @ManyToMany
    @JoinTable(
            name = "jornal_personagens",
            joinColumns = @JoinColumn(name = "jornal_id"),
            inverseJoinColumns = @JoinColumn(name = "personagem_id")
    )
    @Builder.Default
    private List<Personagem> personagens = new ArrayList<>();
}