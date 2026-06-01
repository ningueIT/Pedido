package com.example.pedido.application.service;

import com.example.pedido.application.dto.CriarNovoPedidoCommand;
import com.example.pedido.application.useCase.CriarNovoPedidoUseCase;
import com.example.pedido.domain.model.ItemPedido;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.StatusPedido;
import com.example.pedido.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CriarNovoPedidoService implements CriarNovoPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public CriarNovoPedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional
    public Pedido executar(CriarNovoPedidoCommand command) {
        validar(command);

        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.CRIADO);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CriarNovoPedidoCommand.Item itemCommand : command.itens()) {
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setNomeProduto(itemCommand.nomeProduto());
            item.setQuantidade(itemCommand.quantidade());
            item.setPreco(itemCommand.preco());

            BigDecimal subtotal = itemCommand.preco()
                    .multiply(BigDecimal.valueOf(itemCommand.quantidade()));

            itens.add(item);
            total = total.add(subtotal);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);

        return pedidoRepository.salvar(pedido);
    }

    private void validar(CriarNovoPedidoCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando não pode ser nulo.");
        }
        if (command.itens() == null || command.itens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve possuir ao menos um item.");
        }
        for (CriarNovoPedidoCommand.Item item : command.itens()) {
            if (item.nomeProduto() == null || item.nomeProduto().isBlank()) {
                throw new IllegalArgumentException("nomeProduto é obrigatório.");
            }
            if (item.quantidade() == null || item.quantidade() <= 0) {
                throw new IllegalArgumentException("quantidade deve ser maior que zero.");
            }
            if (item.preco() == null || item.preco().signum() <= 0) {
                throw new IllegalArgumentException("preco deve ser maior que zero.");
            }
        }
    }
}