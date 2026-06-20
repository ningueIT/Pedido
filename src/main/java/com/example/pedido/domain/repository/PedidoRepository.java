package com.example.pedido.domain.repository;

import com.example.pedido.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PedidoRepository {
    Page<Pedido> buscarTodos(Pageable pageable);

    Optional<Pedido> buscarPorId(Long id);

    Pedido salvar(Pedido pedido);
}