package com.example.pedido.presentation.controller;

import com.example.pedido.application.dto.CriarNovoPedidoCommand;
import com.example.pedido.application.useCase.CriarNovoPedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.presentation.dto.CriarNovoPedidoRequest;
import com.example.pedido.presentation.dto.PedidoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final CriarNovoPedidoUseCase criarNovoPedidoUseCase;

    public PedidoController(CriarNovoPedidoUseCase criarNovoPedidoUseCase) {
        this.criarNovoPedidoUseCase = criarNovoPedidoUseCase;
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
                        .collect(Collectors.toList())
        );

        Pedido pedidoCriado = criarNovoPedidoUseCase.executar(command);

        return ResponseEntity
                .created(URI.create("/api/pedidos/" + pedidoCriado.getId()))
                .body(PedidoResponse.from(pedidoCriado));
    }
}