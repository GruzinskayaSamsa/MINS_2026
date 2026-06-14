package org.example.reference;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferenceServerApp {
    private static final Logger log = LoggerFactory.getLogger(ReferenceServerApp.class);

    public static void main(String[] args) throws Exception {
        int port = 6565;
        Server server = ServerBuilder.forPort(port)
                .addService(ServerInterceptors.intercept(new ReferenceServiceImpl(), new TraceIdServerInterceptor()))
                .build()
                .start();
        log.info("Справочный сервис (B) запущен, порт {}", port);
        System.out.println("Reference Service запущен на порту " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
