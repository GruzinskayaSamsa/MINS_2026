package org.example.fitness.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public final class TraceContext {
    private static final Logger log = LoggerFactory.getLogger(TraceContext.class);

    private TraceContext() {
    }

    public static void runWithNewTrace(String operation, Runnable action) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        try {
            log.info("Начата операция: {}", operation);
            action.run();
            log.info("Операция завершена: {}", operation);
        } finally {
            MDC.remove("traceId");
        }
    }
}
