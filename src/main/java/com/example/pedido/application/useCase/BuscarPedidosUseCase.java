package com.example.pedido.application.usecase;

import com.example.pedido.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuscarPedidosUseCase {
    Page<Pedido> buscarTodos(Pageable pageable);

    Pedido buscarPorId(Long id);
}
