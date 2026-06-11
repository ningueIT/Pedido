package com.example.pedido.application.service;

import com.example.pedido.application.useCase.AtualizarStatusPedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.StatusPedido;
import com.example.pedido.domain.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

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
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        StatusPedido statusPedido = converterStatus(novoStatus);
        pedido.atualizarStatus(statusPedido);

        return pedidoRepository.salvar(pedido);
    }

    private StatusPedido converterStatus(String novoStatus) {
        if (novoStatus == null || novoStatus.isBlank()) {
            throw new IllegalArgumentException("Status do pedido é obrigatório.");
        }

        try {
            return StatusPedido.valueOf(novoStatus.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status do pedido inválido: " + novoStatus);
        }
    }
}

