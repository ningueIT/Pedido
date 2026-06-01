package com.example.pedido.application.useCase;

import com.example.pedido.application.dto.CriarNovoPedidoCommand;
import com.example.pedido.domain.model.Pedido;

public interface CriarNovoPedidoUseCase {
    Pedido executar(CriarNovoPedidoCommand command);
}