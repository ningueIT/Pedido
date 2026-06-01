package com.example.pedido.application.service;

import com.example.pedido.application.dto.CriarNovoPedidoCommand;
import com.example.pedido.application.useCase.CriarNovoPedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarNovoPedidoService implements CriarNovoPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public CriarNovoPedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional
    public Pedido executar(CriarNovoPedidoCommand command) {
        Pedido pedido = Pedido.novoPedido();

        for (CriarNovoPedidoCommand.Item item : command.itens()) {
            pedido.adicionarItem(
                    item.nomeProduto(),
                    item.quantidade(),
                    item.preco()
            );
        }

        return pedidoRepository.salvar(pedido);
    }
}