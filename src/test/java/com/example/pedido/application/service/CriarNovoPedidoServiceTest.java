package com.example.pedido.application.service;

import com.example.pedido.application.dto.CriarNovoPedidoCommand;
import com.example.pedido.application.exception.BusinessException;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.StatusPedido;
import com.example.pedido.domain.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarNovoPedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    private CriarNovoPedidoService criarNovoPedidoService;

    @BeforeEach
    void setUp() {
        criarNovoPedidoService = new CriarNovoPedidoService(pedidoRepository);
    }

    @Test
    void deveCriarPedidoComItensValidos() {
        CriarNovoPedidoCommand command = new CriarNovoPedidoCommand(List.of(
                new CriarNovoPedidoCommand.Item("Produto A", 2, BigDecimal.valueOf(10.00)),
                new CriarNovoPedidoCommand.Item("Produto B", 1, BigDecimal.valueOf(5.50))
        ));

        when(pedidoRepository.salvar(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = criarNovoPedidoService.executar(command);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getStatus()).isEqualTo(StatusPedido.CRIADO);
        assertThat(resultado.possuiItens()).isTrue();
        assertThat(resultado.getItens()).hasSize(2);
        assertThat(resultado.getValorTotal()).isEqualByComparingTo("25.50");
        verify(pedidoRepository).salvar(any(Pedido.class));
    }

    @Test
    void deveLancarBusinessExceptionQuandoCommandNulo() {
        assertThatThrownBy(() -> criarNovoPedidoService.executar(null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("nulo");

        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoListaDeItensVazia() {
        CriarNovoPedidoCommand command = new CriarNovoPedidoCommand(Collections.emptyList());

        assertThatThrownBy(() -> criarNovoPedidoService.executar(command))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("item");

        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoListaDeItensNula() {
        CriarNovoPedidoCommand command = new CriarNovoPedidoCommand(null);

        assertThatThrownBy(() -> criarNovoPedidoService.executar(command))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("item");

        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    void devePropagarlllegalArgumentExceptionDeItemInvalido() {
        CriarNovoPedidoCommand command = new CriarNovoPedidoCommand(List.of(
                new CriarNovoPedidoCommand.Item("", 2, BigDecimal.valueOf(10.00)) // nome em branco
        ));

        assertThatThrownBy(() -> criarNovoPedidoService.executar(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nomeProduto");

        verify(pedidoRepository, never()).salvar(any());
    }
}

