package com.example.pedido.application.service;

import com.example.pedido.application.exception.PedidoNaoEncontradoException;
import com.example.pedido.application.usecase.BuscarPedidosUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
public class BuscarPedidosService implements BuscarPedidosUseCase {

    private final PedidoRepository pedidoRepository;

    public BuscarPedidosService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Page<Pedido> buscarTodos(Pageable pageable) {
        return pedidoRepository.buscarTodos(pageable);
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }
}
