package com.Rpg.sistem_mayfair.domain.estabelecimento;

import com.Rpg.sistem_mayfair.domain.Enum.TipoMovimentacaoEstabelecimento;
import com.Rpg.sistem_mayfair.domain.Personagem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "movimentacoes_estabelecimento",
        indexes = {
                @Index(name = "idx_mov_estabelecimento", columnList = "estabelecimento_id"),
                @Index(name = "idx_mov_data", columnList = "dataMovimentacao")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * ESTABELECIMENTO
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estabelecimento_id", nullable = false)
    private Estabelecimento estabelecimento;


    /*
     * TIPO DA MOVIMENTAÇÃO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacaoEstabelecimento tipo;

    /*
     * IMPACTO NA MORAL
     */
    @Column(nullable = false)
    private Integer impactoMoral;

    /*
     * OBSERVAÇÃO
     */
    @Column(columnDefinition = "TEXT")
    private String observacao;

    /*
     * DATA DA MOVIMENTAÇÃO
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataMovimentacao;
}