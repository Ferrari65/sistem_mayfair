package com.Rpg.sistem_mayfair.domain.estabelecimento;

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

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;
}