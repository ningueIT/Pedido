package com.example.pedido.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CriarNovoPedidoRequest(
        @NotEmpty(message = "O pedido deve possuir ao menos um item.")
        List<@Valid ItemRequest> itens
) {
    public record ItemRequest(
            @NotBlank(message = "nomeProduto é obrigatório.")
            String nomeProduto,

            @NotNull(message = "quantidade é obrigatória.")
            @Positive(message = "quantidade deve ser maior que zero.")
            Integer quantidade,

            @NotNull(message = "preco é obrigatório.")
            @DecimalMin(value = "0.01", message = "preco deve ser maior que zero.")
            BigDecimal preco
    ) {}
}