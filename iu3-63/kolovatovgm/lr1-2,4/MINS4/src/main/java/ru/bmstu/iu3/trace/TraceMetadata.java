package ru.bmstu.iu3.trace;

import io.grpc.Metadata;

public final class TraceMetadata {
    public static final Metadata.Key<String> TRACE_ID_KEY =
            Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER);

    private TraceMetadata() {
    }
}
