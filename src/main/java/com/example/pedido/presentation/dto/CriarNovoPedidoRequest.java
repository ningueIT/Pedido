package com.example.pedido.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

public record CriarNovoPedidoRequest(
        List<ItemRequest> itens
) {
    public record ItemRequest(
            String nomeProduto,
            Integer quantidade,
            BigDecimal preco
    ) {}
}