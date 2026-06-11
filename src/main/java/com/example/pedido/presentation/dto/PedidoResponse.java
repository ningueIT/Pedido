package com.example.pedido.presentation.dto;

import com.example.pedido.domain.model.ItemPedido;
import com.example.pedido.domain.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String status,
        BigDecimal valorTotal,
        LocalDateTime dataPedido,
        List<ItemPedidoResponse> itens
) {
    public record ItemPedidoResponse(
            Long id,
            String nomeProduto,
            Integer quantidade,
            BigDecimal preco,
            BigDecimal subtotal
    ) {
        public static ItemPedidoResponse from(ItemPedido item) {
            return new ItemPedidoResponse(
                    item.getId(),
                    item.getNomeProduto(),
                    item.getQuantidade(),
                    item.getPreco(),
                    item.calcularSubtotal()
            );
        }
    }

    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getStatus().name(),
                pedido.getValorTotal(),
                pedido.getDataPedido(),
                pedido.getItens().stream()
                        .map(ItemPedidoResponse::from)
                        .toList()
        );
    }
}