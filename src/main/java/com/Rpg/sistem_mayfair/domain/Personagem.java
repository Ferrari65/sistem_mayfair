package com.Rpg.sistem_mayfair.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Familia familia;

//    @OneToMany(mappedBy = "personagem", cascade = CascadeType.ALL)
//    private List<HistoricoPrestigio> historicoPrestigio;

    public void alterarPrestigio(int quantidade) {
        int novoPrestigio = this.prestigio + quantidade;
        if (novoPrestigio > 50) this.prestigio = 50;
        else if (novoPrestigio < 0) this.prestigio = 0;
        else this.prestigio = novoPrestigio;
    }
}