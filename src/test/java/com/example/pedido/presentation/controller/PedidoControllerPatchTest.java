package com.example.pedido.presentation.controller;

import com.example.pedido.application.useCase.AtualizarStatusPedidoUseCase;
import com.example.pedido.application.useCase.BuscarPedidosUseCase;
import com.example.pedido.application.useCase.CriarNovoPedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.StatusPedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
class PedidoControllerPatchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CriarNovoPedidoUseCase criarNovoPedidoUseCase;

    @MockitoBean
    private BuscarPedidosUseCase buscarPedidosUseCase;

    @MockitoBean
    private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    @Test
    void deveAtualizarStatusDoPedidoViaEndpointPatch() throws Exception {
        Pedido pedido = Pedido.novoPedido();
        pedido.atualizarStatus(StatusPedido.CONFIRMADO);

        when(atualizarStatusPedidoUseCase.executar(1L, "confirmado")).thenReturn(pedido);

        mockMvc.perform(patch("/api/pedidos/{id}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "confirmado"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMADO"))
                .andExpect(jsonPath("$.itens").isArray());

        verify(atualizarStatusPedidoUseCase).executar(1L, "confirmado");
    }

    @Test
    void deveRetornarBadRequestQuandoStatusForBlank() throws Exception {
        mockMvc.perform(patch("/api/pedidos/{id}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "   "
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));

        verifyNoInteractions(atualizarStatusPedidoUseCase);
    }
}

