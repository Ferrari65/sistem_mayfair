package com.Rpg.sistem_mayfair.domain.estabelecimento;

import com.Rpg.sistem_mayfair.domain.Enum.TipoAmbiente;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ambientes_estabelecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbienteEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    /*
     * tipo opcional (ANDAR, SALA, ALA, JARDIM...)
     */
    @Enumerated(EnumType.STRING)
    private TipoAmbiente tipo;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;

    /*
     * FOTOS DO AMBIENTE
     * (corrigido: precisa de relação reversa depois se quiser avançar)
     */
    @OneToMany(mappedBy = "ambiente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoAmbiente> fotos = new ArrayList<>();
}