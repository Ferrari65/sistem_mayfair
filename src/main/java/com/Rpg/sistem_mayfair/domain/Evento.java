package com.Rpg.sistem_mayfair.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tb_eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Boolean finalizado = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "finalizado_at")
    private LocalDateTime finalizadoAt;

    public Evento() {
    }

    public Evento(Long id, String titulo, String descricao, Boolean finalizado,
                  LocalDateTime createdAt, LocalDateTime finalizadoAt) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.finalizado = finalizado;
        this.createdAt = createdAt;
        this.finalizadoAt = finalizadoAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void finalizar() {
        this.finalizado = true;
        this.finalizadoAt = LocalDateTime.now();
    }
}