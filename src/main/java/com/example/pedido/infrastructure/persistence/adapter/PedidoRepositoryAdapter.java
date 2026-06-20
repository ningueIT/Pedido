package com.example.pedido.infrastructure.persistence.adapter;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.repository.PedidoRepository;
import com.example.pedido.infrastructure.persistence.jpa.PedidoJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final PedidoJpaRepository pedidoJpaRepository;

    public PedidoRepositoryAdapter(PedidoJpaRepository pedidoJpaRepository) {
        this.pedidoJpaRepository = pedidoJpaRepository;
    }

    @Override
    public Page<Pedido> buscarTodos(Pageable pageable) {
        return pedidoJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoJpaRepository.findById(id);
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        return pedidoJpaRepository.save(pedido);
    }
}