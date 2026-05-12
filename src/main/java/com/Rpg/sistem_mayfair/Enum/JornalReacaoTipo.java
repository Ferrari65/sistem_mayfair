package com.Rpg.sistem_mayfair.Enum;

public enum JornalReacaoTipo {

    ALGUEM_CHAME_O_VIGARIO(
            " Alguém chame o vigário, eu não aguento mais!"),

    INTELIGENCIA_EM_GREVE(
            " A inteligência está em greve desde a temporada passada"),

    MORAL_DESAPARECEU(
            " Minha visão ficou turva, ou foi só a moral dela que sumiu?"),

    ALTO_TEOR_DE_VENENO(
            " Cuidado: notícia com alto teor de veneno."),

    O_CHA_ESTA_QUENTE(
            " O Chá Está Quente"),

    SEM_NOCAO_DO_ANO(
            " E o prêmio de 'Sem Noção do Ano' vai para..."),

    O_OLHO_NAO_VE(
            " O que o olho não vê, a gente publica no jornal"),

    ABAFA_O_CASO(
            " Abafa o caso, ou melhor, publica na capa!"),

    FOFOQUEIROS_MERECEM_SOBREMESA(
            " Fofoqueiros merecem sobremesa"),

    O_CHA_TA_SERVIDO(
            " O chá tá SERVIDO"),

    EU_FINGINDO_QUE_NAO_VI(
            " eu fingindo que não vi");

    private final String descricao;

    JornalReacaoTipo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}