package com.Rpg.sistem_mayfair.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String foto;

    @ManyToOne
    @JoinColumn(name = "id_familia")
    @JsonBackReference
    private Familia familia;

    @Column(nullable = true, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "personagem", cascade = CascadeType.ALL)
    private List<HistoricoPrestigio> historicoPrestigio;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void alterarPrestigio(int quantidade) {

        int novoPrestigio = this.prestigio + quantidade;

        if (novoPrestigio > 50) {
            this.prestigio = 50;
        } else if (novoPrestigio < 0) {
            this.prestigio = 0;
        } else {
            this.prestigio = novoPrestigio;
        }
    }
}