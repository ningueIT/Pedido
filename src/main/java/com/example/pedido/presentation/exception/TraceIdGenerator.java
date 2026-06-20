package com.example.pedido.presentation.exception;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public final class TraceIdGenerator {

    private static final String TRACE_ID_HEADER = "X-Correlation-Id";
    public static final String TRACE_ID_ATTRIBUTE = "traceId";

    private TraceIdGenerator() {
    }

    public static String getOrGenerate(HttpServletRequest request) {
        Object attrTraceId = request.getAttribute(TRACE_ID_ATTRIBUTE);
        if (attrTraceId != null) {
            return attrTraceId.toString();
        }
        String traceId = request.getHeader(TRACE_ID_HEADER);

        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        return traceId;
    }
}