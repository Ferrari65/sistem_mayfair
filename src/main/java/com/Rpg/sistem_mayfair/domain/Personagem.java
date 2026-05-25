package com.Rpg.sistem_mayfair.domain;

import com.Rpg.sistem_mayfair.domain.Enum.Genero;
import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import com.Rpg.sistem_mayfair.domain.jornal.JornalPostagem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private String shape;

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonBackReference
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero = Genero.NAO_INFORMADO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCivil statusCivil = StatusCivil.SOLTEIRO;

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

    @ManyToMany(mappedBy = "personagens")
    private List<JornalPostagem> jornais = new ArrayList<>();

    @PreRemove
    private void removerAssociacoesJornais() {
        for (JornalPostagem jornal : jornais) {
            jornal.getPersonagens().remove(this);
        }
    }


    @ManyToOne
    @JoinColumn(name = "parceiro_id")
    @JsonIgnoreProperties({"parceiro", "historicoPrestigio", "player"})
    private Personagem parceiro;

    private Boolean diamanteTemporada = false;
}