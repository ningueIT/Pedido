package com.example.pedido.application.useCase;

import com.example.pedido.domain.model.Pedido;

import java.util.List;

public interface BuscarPedidosUseCase {
    List<Pedido> buscarTodos();

    Pedido buscarPorId(Long id);
}

