package com.example.pedido.infrastructure.persistence.jpa;

import com.example.pedido.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoJpaRepository extends JpaRepository<Pedido, Long> {
}