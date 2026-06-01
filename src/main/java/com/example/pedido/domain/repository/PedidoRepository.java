package com.example.pedido.domain.repository;

import com.example.pedido.domain.model.Pedido;


public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
}