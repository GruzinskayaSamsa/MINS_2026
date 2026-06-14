package org.example.reference;

import io.grpc.*;

public class TraceIdServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        String traceId = TraceKeys.safeTraceId(headers.get(TraceKeys.TRACE_ID_HEADER));
        Context context = Context.current().withValue(TraceKeys.TRACE_ID_CONTEXT, traceId);
        return Contexts.interceptCall(context, call, headers, next);
    }
}
