package com.example.pedido.domain.model;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public enum StatusPedido {
    CRIADO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    private static final Map<StatusPedido, Set<StatusPedido>> TRANSICOES_VALIDAS = Map.of(
            CRIADO,            EnumSet.of(CONFIRMADO, CANCELADO),
            CONFIRMADO,        EnumSet.of(EM_PREPARO, CANCELADO),
            EM_PREPARO,        EnumSet.of(SAIU_PARA_ENTREGA, CANCELADO),
            SAIU_PARA_ENTREGA, EnumSet.of(ENTREGUE),
            ENTREGUE,          EnumSet.noneOf(StatusPedido.class),
            CANCELADO,         EnumSet.noneOf(StatusPedido.class)
    );

    public boolean podeTransitarPara(StatusPedido novoStatus) {
        return TRANSICOES_VALIDAS.getOrDefault(this, EnumSet.noneOf(StatusPedido.class))
                .contains(novoStatus);
    }

    public static StatusPedido fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Status do pedido é obrigatório.");
        }
        try {
            return StatusPedido.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status do pedido inválido: " + valor);
        }
    }
}