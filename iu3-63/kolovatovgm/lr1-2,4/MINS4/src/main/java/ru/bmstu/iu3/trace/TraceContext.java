package ru.bmstu.iu3.trace;

import io.grpc.Context;

public final class TraceContext {
    public static final Context.Key<String> TRACE_ID = Context.key("traceId");

    private TraceContext() {
    }

    public static String currentTraceId() {
        String traceId = TRACE_ID.get();
        return traceId == null ? "no-trace" : traceId;
    }
}
