package com.example.pedido.application.useCase;

import com.example.pedido.domain.model.Pedido;

public interface AtualizarStatusPedidoUseCase {
    Pedido executar(Long id, String novoStatus);
}

