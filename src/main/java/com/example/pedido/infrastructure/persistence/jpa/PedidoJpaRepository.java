package com.example.pedido.infrastructure.persistence.jpa;

import com.example.pedido.domain.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PedidoJpaRepository extends JpaRepository<Pedido, Long> {
	@Override
	@NonNull
	@EntityGraph(attributePaths = "itens")
	List<Pedido> findAll();

	@Override
	@NonNull
	@EntityGraph(attributePaths = "itens")
	Optional<Pedido> findById(@NonNull Long id);
}