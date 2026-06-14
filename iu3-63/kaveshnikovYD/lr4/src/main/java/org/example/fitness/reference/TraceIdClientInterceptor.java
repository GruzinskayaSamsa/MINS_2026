package org.example.fitness.reference;

import io.grpc.*;
import org.slf4j.MDC;

public class TraceIdClientInterceptor implements ClientInterceptor {
    private static final Metadata.Key<String> TRACE_ID_KEY =
            Metadata.Key.of("x-trace-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = MDC.get("traceId");
                if (traceId != null && !traceId.isBlank()) {
                    headers.put(TRACE_ID_KEY, traceId);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
