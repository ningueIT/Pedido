package com.example.pedido.presentation.controller;

import com.example.pedido.application.exception.PedidoNaoEncontradoException;
import com.example.pedido.application.usecase.AtualizarStatusPedidoUseCase;
import com.example.pedido.application.usecase.BuscarPedidosUseCase;
import com.example.pedido.application.usecase.CriarNovoPedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
class PedidoControllerGetPostTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CriarNovoPedidoUseCase criarNovoPedidoUseCase;

    @MockitoBean
    private BuscarPedidosUseCase buscarPedidosUseCase;

    @MockitoBean
    private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    // ---- GET /api/pedidos ----

    @Test
    void deveListarPedidosComPaginacaoERetornar200() throws Exception {
        Pedido pedido = Pedido.novoPedido();
        pedido.adicionarItem("Produto A", 1, BigDecimal.valueOf(20.00));
        Page<Pedido> page = new PageImpl<>(List.of(pedido));

        when(buscarPedidosUseCase.buscarTodos(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].status").value("CRIADO"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(buscarPedidosUseCase).buscarTodos(any(Pageable.class));
    }

    @Test
    void deveAceitarParametrosDePaginacao() throws Exception {
        Page<Pedido> page = new PageImpl<>(List.of());

        when(buscarPedidosUseCase.buscarTodos(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/pedidos")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // ---- GET /api/pedidos/{id} ----

    @Test
    void deveBuscarPedidoPorIdERetornar200() throws Exception {
        Pedido pedido = Pedido.novoPedido();

        when(buscarPedidosUseCase.buscarPorId(1L)).thenReturn(pedido);

        mockMvc.perform(get("/api/pedidos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CRIADO"))
                .andExpect(jsonPath("$.itens").isArray());

        verify(buscarPedidosUseCase).buscarPorId(1L);
    }

    @Test
    void deveRetornar404QuandoPedidoNaoExistir() throws Exception {
        when(buscarPedidosUseCase.buscarPorId(99L))
                .thenThrow(new PedidoNaoEncontradoException(99L));

        mockMvc.perform(get("/api/pedidos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(404));
    }

    // ---- POST /api/pedidos ----

    @Test
    void deveCriarPedidoERetornar201() throws Exception {
        Pedido pedido = Pedido.novoPedido();
        pedido.adicionarItem("Produto A", 2, BigDecimal.valueOf(15.00));

        when(criarNovoPedidoUseCase.executar(any())).thenReturn(pedido);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "itens": [
                                    {
                                      "nomeProduto": "Produto A",
                                      "quantidade": 2,
                                      "preco": 15.00
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CRIADO"))
                .andExpect(jsonPath("$.valorTotal").value(30.00))
                .andExpect(jsonPath("$.itens.length()").value(1));

        verify(criarNovoPedidoUseCase).executar(any());
    }

    @Test
    void deveRetornar400QuandoCriarPedidoSemItens() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "itens": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void deveRetornar400QuandoCriarPedidoComItemSemNome() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "itens": [
                                    {
                                      "nomeProduto": "",
                                      "quantidade": 1,
                                      "preco": 10.00
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }
}

