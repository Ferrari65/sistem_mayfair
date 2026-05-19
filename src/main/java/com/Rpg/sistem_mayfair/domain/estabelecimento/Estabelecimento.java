package com.Rpg.sistem_mayfair.domain.estabelecimento;

import com.Rpg.sistem_mayfair.domain.Personagem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estabelecimentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * INFORMAÇÕES BÁSICAS
     */
    @Column(nullable = false)
    private String nomeLocal;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    /*
     * MORAL
     */
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer moral = 50;

    /*
     * DINHEIRO
     */
    @Column(nullable = false)
    private Double dinheiro = 0.0;

    /*
     * MOVIMENTAÇÕES
     */
    @OneToMany(
            mappedBy = "estabelecimento",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MovimentacaoEstabelecimento> movimentacoes = new ArrayList<>();

    /*
     * ESTATÍSTICAS
     */
    @Column(nullable = false)
    private Long totalMovimentacoes = 0L;

    /*
     * =========================
     * MOVIMENTAÇÕES
     * =========================
     */
    public void registrarMovimentacao() {
        this.totalMovimentacoes++;
    }

    /*
     * HORÁRIOS
     */
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;

    /*
     * PROPRIETÁRIO
     */
    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    @JsonIgnoreProperties({
            "historicoPrestigio",
            "player",
            "familia"
    })
    private Personagem proprietario;

    /*
     * FUNCIONÁRIOS
     */
    @ManyToMany
    @JoinTable(
            name = "estabelecimento_funcionarios",
            joinColumns = @JoinColumn(name = "estabelecimento_id"),
            inverseJoinColumns = @JoinColumn(name = "personagem_id")
    )
    @JsonIgnoreProperties({
            "historicoPrestigio",
            "player",
            "familia"
    })
    private List<Personagem> funcionarios = new ArrayList<>();

    /*
     * FOTO PRINCIPAL (1:1)
     */
    @OneToOne(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private FotoEstabelecimento fotos;

    /*
     * =========================
     * MORAL
     * =========================
     */
    public void alterarMoral(int quantidade) {
        int novoValor = this.moral + quantidade;
        this.moral = Math.max(0, Math.min(100, novoValor));
    }

    public void aumentarMoral(int quantidade) {
        alterarMoral(Math.abs(quantidade));
    }

    public void reduzirMoral(int quantidade) {
        alterarMoral(-Math.abs(quantidade));
    }

    /*
     * =========================
     * DINHEIRO
     * =========================
     */
    public void alterarDinheiro(double valor) {
        double novoValor = this.dinheiro + valor;
        this.dinheiro = Math.max(0, novoValor);
    }

    public void adicionarDinheiro(double valor) {
        alterarDinheiro(Math.abs(valor));
    }

    public void removerDinheiro(double valor) {
        alterarDinheiro(-Math.abs(valor));
    }
}