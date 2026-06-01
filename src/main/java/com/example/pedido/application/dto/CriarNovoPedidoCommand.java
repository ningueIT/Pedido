package com.example.pedido.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record CriarNovoPedidoCommand(
        List<Item> itens
) {
    public record Item(
            String nomeProduto,
            Integer quantidade,
            BigDecimal preco
    ) {
    }
}