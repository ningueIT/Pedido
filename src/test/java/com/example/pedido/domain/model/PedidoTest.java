package com.example.pedido.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PedidoTest {

    @Test
    void novoPedidoDeveTerStatusCriadoEValorTotalZero() {
        Pedido pedido = Pedido.novoPedido();

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CRIADO);
        assertThat(pedido.getValorTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(pedido.possuiItens()).isFalse();
    }

    @Test
    void adicionarItemDeveAtualizarValorTotalEListaDeItens() {
        Pedido pedido = Pedido.novoPedido();

        pedido.adicionarItem("Produto A", 2, BigDecimal.valueOf(10.00));

        assertThat(pedido.getValorTotal()).isEqualByComparingTo("20.00");
        assertThat(pedido.getItens()).hasSize(1);
        assertThat(pedido.possuiItens()).isTrue();
    }

    @Test
    void adicionarDoisItensDeveAcumularValorTotal() {
        Pedido pedido = Pedido.novoPedido();

        pedido.adicionarItem("Produto A", 1, BigDecimal.valueOf(30.00));
        pedido.adicionarItem("Produto B", 3, BigDecimal.valueOf(5.00));

        assertThat(pedido.getValorTotal()).isEqualByComparingTo("45.00");
        assertThat(pedido.getItens()).hasSize(2);
    }

    @Test
    void adicionarItemNuloDeveLancarIllegalArgumentException() {
        Pedido pedido = Pedido.novoPedido();

        assertThatThrownBy(() -> pedido.adicionarItem((ItemPedido) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo");
    }

    @Test
    void removerItemDeveAtualizarValorTotal() {
        Pedido pedido = Pedido.novoPedido();
        pedido.adicionarItem("Produto A", 2, BigDecimal.valueOf(10.00));

        ItemPedido item = pedido.getItens().get(0);
        pedido.removerItem(item);

        assertThat(pedido.getValorTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(pedido.possuiItens()).isFalse();
    }

    @Test
    void atualizarStatusDeveAceitarTransicoesValidas() {
        Pedido pedido = Pedido.novoPedido();

        pedido.atualizarStatus(StatusPedido.CONFIRMADO);
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CONFIRMADO);

        pedido.atualizarStatus(StatusPedido.EM_PREPARO);
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.EM_PREPARO);

        pedido.atualizarStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedido.atualizarStatus(StatusPedido.ENTREGUE);
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.ENTREGUE);
    }

    @Test
    void atualizarStatusDeveRejeitarTransicaoInvalida() {
        Pedido pedido = Pedido.novoPedido(); // CRIADO

        assertThatThrownBy(() -> pedido.atualizarStatus(StatusPedido.ENTREGUE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Transição de status inválida");
    }

    @Test
    void atualizarStatusDeveRejeitarRetornoDeStatusFinal() {
        Pedido pedido = Pedido.novoPedido();
        pedido.atualizarStatus(StatusPedido.CONFIRMADO);
        pedido.atualizarStatus(StatusPedido.EM_PREPARO);
        pedido.atualizarStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedido.atualizarStatus(StatusPedido.ENTREGUE);

        assertThatThrownBy(() -> pedido.atualizarStatus(StatusPedido.CRIADO))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void atualizarStatusNuloDeveLancarIllegalArgumentException() {
        Pedido pedido = Pedido.novoPedido();

        assertThatThrownBy(() -> pedido.atualizarStatus(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo");
    }

    @Test
    void naoDeveAdicionarItemEmPedidoEntregue() {
        Pedido pedido = pedidoEntregue();

        assertThatThrownBy(() -> pedido.adicionarItem("X", 1, BigDecimal.ONE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ENTREGUE");
    }

    @Test
    void naoDeveAdicionarItemEmPedidoCancelado() {
        Pedido pedido = Pedido.novoPedido();
        pedido.atualizarStatus(StatusPedido.CANCELADO);

        assertThatThrownBy(() -> pedido.adicionarItem("X", 1, BigDecimal.ONE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CANCELADO");
    }

    @Test
    void naoDeveRemoverItemDeObjetoCancelado() {
        Pedido pedido = Pedido.novoPedido();
        pedido.atualizarStatus(StatusPedido.CANCELADO);

        // validarPedidoModificavel() é chamado antes do null-check, então mesmo null lança exceção
        assertThatThrownBy(() -> pedido.removerItem(null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CANCELADO");
    }

    @Test
    void getItensDeveRetornarListaImutavel() {
        Pedido pedido = Pedido.novoPedido();
        pedido.adicionarItem("X", 1, BigDecimal.ONE);

        assertThatThrownBy(() -> pedido.getItens().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // ------ helpers ------

    private Pedido pedidoEntregue() {
        Pedido pedido = Pedido.novoPedido();
        pedido.atualizarStatus(StatusPedido.CONFIRMADO);
        pedido.atualizarStatus(StatusPedido.EM_PREPARO);
        pedido.atualizarStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedido.atualizarStatus(StatusPedido.ENTREGUE);
        return pedido;
    }
}

