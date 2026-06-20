package com.example.pedido.application.service;

import com.example.pedido.application.exception.PedidoNaoEncontradoException;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarPedidosServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    private BuscarPedidosService buscarPedidosService;

    @BeforeEach
    void setUp() {
        buscarPedidosService = new BuscarPedidosService(pedidoRepository);
    }

    @Test
    void deveBuscarTodosOsPedidosPaginados() {
        Pedido pedido1 = Pedido.novoPedido();
        Pedido pedido2 = Pedido.novoPedido();
        List<Pedido> pedidos = List.of(pedido1, pedido2);
        Pageable pageable = Pageable.unpaged();
        Page<Pedido> page = new PageImpl<>(pedidos, pageable, pedidos.size());

        when(pedidoRepository.buscarTodos(pageable)).thenReturn(page);

        Page<Pedido> resultado = buscarPedidosService.buscarTodos(pageable);

        assertThat(resultado.getContent()).containsExactlyElementsOf(pedidos);
        assertThat(resultado.getTotalElements()).isEqualTo(2);
        verify(pedidoRepository).buscarTodos(pageable);
    }

    @Test
    void deveBuscarPedidoPorIdQuandoExistir() {
        Long id = 1L;
        Pedido pedido = Pedido.novoPedido();

        when(pedidoRepository.buscarPorId(id)).thenReturn(Optional.of(pedido));

        Pedido resultado = buscarPedidosService.buscarPorId(id);

        assertThat(resultado).isSameAs(pedido);
        verify(pedidoRepository).buscarPorId(id);
    }

    @Test
    void deveLancarPedidoNaoEncontradoExceptionQuandoPedidoNaoExistir() {
        Long id = 99L;

        when(pedidoRepository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarPedidosService.buscarPorId(id))
                .isInstanceOf(PedidoNaoEncontradoException.class)
                .hasMessageContaining("Pedido não encontrado");

        verify(pedidoRepository).buscarPorId(id);
    }
}
