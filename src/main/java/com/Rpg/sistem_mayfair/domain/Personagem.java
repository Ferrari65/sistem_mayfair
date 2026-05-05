package com.Rpg.sistem_mayfair.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "personagens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_personagens;

    private String nome;

    private Integer idade;

    private String titulo;

    @Min(0)
    @Max(50)
    @Column(nullable = false)
    private Integer prestigio = 20;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "id_familia")
    @JsonIgnoreProperties("personagens")
    private Familia familia;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "personagem", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("personagem")
    private List<HistoricoPrestigio> historicoPrestigio;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void alterarPrestigio(int quantidade) {
        int novo = this.prestigio + quantidade;
        this.prestigio = Math.max(0, Math.min(50, novo));
    }
}