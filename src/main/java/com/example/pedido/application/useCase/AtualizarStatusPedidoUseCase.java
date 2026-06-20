package com.example.pedido.application.usecase;

import com.example.pedido.domain.model.Pedido;

public interface AtualizarStatusPedidoUseCase {
    Pedido executar(Long id, String novoStatus);
}

