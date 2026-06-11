package com.example.pedido.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizarStatusRequest(
        @NotBlank(message = "status é obrigatório.")
        String status
) {
}

