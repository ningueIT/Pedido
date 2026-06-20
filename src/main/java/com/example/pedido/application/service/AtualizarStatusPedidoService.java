package com.example.pedido.application.service;

import com.example.pedido.application.exception.PedidoNaoEncontradoException;
import com.example.pedido.application.usecase.AtualizarStatusPedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.StatusPedido;
import com.example.pedido.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AtualizarStatusPedidoService implements AtualizarStatusPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public AtualizarStatusPedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido executar(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        StatusPedido statusPedido = StatusPedido.fromString(novoStatus);
        pedido.atualizarStatus(statusPedido);

        return pedidoRepository.salvar(pedido);
    }
}
