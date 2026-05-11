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

    private String imageUrl;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "estabelecimento_id", unique = true)
    private Estabelecimento estabelecimento;
}