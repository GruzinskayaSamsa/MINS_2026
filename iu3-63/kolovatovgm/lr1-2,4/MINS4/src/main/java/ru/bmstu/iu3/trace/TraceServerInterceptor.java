package ru.bmstu.iu3.trace;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import java.util.UUID;
import java.util.logging.Logger;

public class TraceServerInterceptor implements ServerInterceptor {
    private static final Logger LOGGER = Logger.getLogger(TraceServerInterceptor.class.getName());

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        String traceId = headers.get(TraceMetadata.TRACE_ID_KEY);
        if (traceId == null || traceId.trim().isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }
        LOGGER.info("traceId=" + traceId + " incoming gRPC call " + call.getMethodDescriptor().getFullMethodName());
        Context context = Context.current().withValue(TraceContext.TRACE_ID, traceId);
        return Contexts.interceptCall(context, call, headers, next);
    }
}
