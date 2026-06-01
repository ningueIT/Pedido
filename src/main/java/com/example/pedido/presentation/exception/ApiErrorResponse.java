package com.example.pedido.presentation.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String errorCode,
        String message,
        List<String> details,
        String path,
        String traceId
) {
}