package org.example.reference;

import io.grpc.Context;
import io.grpc.Metadata;

import java.util.UUID;

public final class TraceKeys {
    public static final Metadata.Key<String> TRACE_ID_HEADER =
            Metadata.Key.of("x-trace-id", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> TRACE_ID_CONTEXT = Context.key("traceId");

    private TraceKeys() {
    }

    public static String safeTraceId(String value) {
        if (value == null || value.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return value;
    }
}
