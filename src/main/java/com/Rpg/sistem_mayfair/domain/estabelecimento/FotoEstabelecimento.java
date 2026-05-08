package com.Rpg.sistem_mayfair.domain.estabelecimento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "foto_estabelecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FotoEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * URL DA IMAGEM (CLOUDINARY)
     */
    private String imageUrl;

    /*
     * RELAÇÃO 1:1 REAL (SOBRESCRITA GARANTIDA)
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "estabelecimento_id", unique = true)
    private Estabelecimento estabelecimento;
}