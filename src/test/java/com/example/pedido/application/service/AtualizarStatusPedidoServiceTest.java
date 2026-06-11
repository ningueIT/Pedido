package com.example.pedido.application.service;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.StatusPedido;
import com.example.pedido.domain.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusPedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    private AtualizarStatusPedidoService atualizarStatusPedidoService;

    @BeforeEach
    void setUp() {
        atualizarStatusPedidoService = new AtualizarStatusPedidoService(pedidoRepository);
    }

    @Test
    void deveAtualizarStatusDoPedidoQuandoStatusForValido() {
        Long id = 1L;
        Pedido pedido = Pedido.novoPedido();

        when(pedidoRepository.buscarPorId(id)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.salvar(pedido)).thenReturn(pedido);

        Pedido resultado = atualizarStatusPedidoService.executar(id, "confirmado");

        assertThat(resultado).isSameAs(pedido);
        assertThat(resultado.getStatus()).isEqualTo(StatusPedido.CONFIRMADO);
        verify(pedidoRepository).buscarPorId(id);
        verify(pedidoRepository).salvar(pedido);
    }

    @Test
    void deveLancarEntityNotFoundExceptionQuandoPedidoNaoExistir() {
        Long id = 99L;

        when(pedidoRepository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> atualizarStatusPedidoService.executar(id, "ENTREGUE"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Pedido não encontrado");

        verify(pedidoRepository).buscarPorId(id);
    }

    @Test
    void deveLancarIllegalArgumentExceptionQuandoStatusForInvalido() {
        Long id = 1L;
        Pedido pedido = Pedido.novoPedido();

        when(pedidoRepository.buscarPorId(id)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> atualizarStatusPedidoService.executar(id, "status-inexistente"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Status do pedido inválido: status-inexistente");

        verify(pedidoRepository).buscarPorId(id);
    }
}

