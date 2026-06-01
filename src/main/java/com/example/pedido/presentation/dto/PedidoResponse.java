package com.example.pedido.presentation.dto;

import com.example.pedido.domain.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponse(
        Long id,
        String status,
        BigDecimal valorTotal,
        LocalDateTime dataPedido
) {
    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getStatus().name(),
                pedido.getValorTotal(),
                pedido.getDataPedido()
        );
    }
}