package com.example.pedido.infrastructure.persistence.adapter;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.repository.PedidoRepository;
import com.example.pedido.infrastructure.persistence.jpa.PedidoJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final PedidoJpaRepository pedidoJpaRepository;

    public PedidoRepositoryAdapter(PedidoJpaRepository pedidoJpaRepository) {
        this.pedidoJpaRepository = pedidoJpaRepository;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        return pedidoJpaRepository.save(pedido);
    }
}