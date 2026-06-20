package com.example.pedido.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemPedidoTest {

    @Test
    void novoItemDeveSerCriadoComDadosValidos() {
        ItemPedido item = ItemPedido.novoItem("Produto A", 3, BigDecimal.valueOf(5.00));

        assertThat(item.getNomeProduto()).isEqualTo("Produto A");
        assertThat(item.getQuantidade()).isEqualTo(3);
        assertThat(item.getPreco()).isEqualByComparingTo("5.00");
    }

    @Test
    void calcularSubtotalDeveRetornarPrecoMultiplicadoPorQuantidade() {
        ItemPedido item = ItemPedido.novoItem("Produto B", 4, BigDecimal.valueOf(7.50));

        assertThat(item.calcularSubtotal()).isEqualByComparingTo("30.00");
    }

    @Test
    void novoItemComNomeNuloDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem(null, 1, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nomeProduto");
    }

    @Test
    void novoItemComNomeBrancoDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem("   ", 1, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nomeProduto");
    }

    @Test
    void novoItemComQuantidadeNulaDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem("Produto", null, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantidade");
    }

    @Test
    void novoItemComQuantidadeZeroDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem("Produto", 0, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantidade");
    }

    @Test
    void novoItemComQuantidadeNegativaDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem("Produto", -5, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantidade");
    }

    @Test
    void novoItemComPrecoNuloDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem("Produto", 1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("preco");
    }

    @Test
    void novoItemComPrecoZeroDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem("Produto", 1, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("preco");
    }

    @Test
    void novoItemComPrecoNegativoDeveLancarExcecao() {
        assertThatThrownBy(() -> ItemPedido.novoItem("Produto", 1, BigDecimal.valueOf(-1.00)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("preco");
    }

    @Test
    void alterarQuantidadeDeveAtualizarValor() {
        ItemPedido item = ItemPedido.novoItem("Produto", 2, BigDecimal.TEN);
        item.alterarQuantidade(5);

        assertThat(item.getQuantidade()).isEqualTo(5);
        assertThat(item.calcularSubtotal()).isEqualByComparingTo("50.00");
    }

    @Test
    void alterarQuantidadeZeroDeveLancarExcecao() {
        ItemPedido item = ItemPedido.novoItem("Produto", 2, BigDecimal.TEN);

        assertThatThrownBy(() -> item.alterarQuantidade(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantidade");
    }
}

