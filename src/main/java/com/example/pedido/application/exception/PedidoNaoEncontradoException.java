package com.example.pedido.application.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(Long id) {
        super("Pedido não encontrado: " + id);
    }
}

