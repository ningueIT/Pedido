package com.example.pedido.domain.repository;

import com.example.pedido.domain.model.Pedido;

import java.util.List;
import java.util.Optional;


public interface PedidoRepository {
    List<Pedido> buscarTodos();

    Optional<Pedido> buscarPorId(Long id);

    Pedido salvar(Pedido pedido);
}