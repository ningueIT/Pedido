package com.example.pedido.presentation.controller;

import com.example.pedido.application.dto.CriarNovoPedidoCommand;
import com.example.pedido.application.useCase.AtualizarStatusPedidoUseCase;
import com.example.pedido.application.useCase.BuscarPedidosUseCase;
import com.example.pedido.application.useCase.CriarNovoPedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.presentation.dto.AtualizarStatusRequest;
import com.example.pedido.presentation.dto.CriarNovoPedidoRequest;
import com.example.pedido.presentation.dto.PedidoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final CriarNovoPedidoUseCase criarNovoPedidoUseCase;
    private final BuscarPedidosUseCase buscarPedidosUseCase;
    private final AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    public PedidoController(
            CriarNovoPedidoUseCase criarNovoPedidoUseCase,
            BuscarPedidosUseCase buscarPedidosUseCase,
            AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase
    ) {
        this.criarNovoPedidoUseCase = criarNovoPedidoUseCase;
        this.buscarPedidosUseCase = buscarPedidosUseCase;
        this.atualizarStatusPedidoUseCase = atualizarStatusPedidoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<PedidoResponse> pedidos = buscarPedidosUseCase.buscarTodos().stream()
                .map(PedidoResponse::from)
                .toList();

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        Pedido pedido = buscarPedidosUseCase.buscarPorId(id);
        return ResponseEntity.ok(PedidoResponse.from(pedido));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusRequest request
    ) {
        Pedido pedidoAtualizado = atualizarStatusPedidoUseCase.executar(id, request.status());
        return ResponseEntity.ok(PedidoResponse.from(pedidoAtualizado));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody CriarNovoPedidoRequest request) {
        CriarNovoPedidoCommand command = new CriarNovoPedidoCommand(
                request.itens().stream()
                        .map(item -> new CriarNovoPedidoCommand.Item(
                                item.nomeProduto(),
                                item.quantidade(),
                                item.preco()
                        ))
                        .toList()
        );

        Pedido pedidoCriado = criarNovoPedidoUseCase.executar(command);

        return ResponseEntity
                .created(URI.create("/api/pedidos/" + pedidoCriado.getId()))
                .body(PedidoResponse.from(pedidoCriado));
    }
}