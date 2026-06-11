package com.example.pedido.application.service;

import com.example.pedido.application.useCase.BuscarPedidosUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BuscarPedidosService implements BuscarPedidosUseCase {

    private final PedidoRepository pedidoRepository;

    public BuscarPedidosService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> buscarTodos() {
        return pedidoRepository.buscarTodos();
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }
}

