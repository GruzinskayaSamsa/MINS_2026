package ru.bmstu.iu3.reference;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import ru.bmstu.iu3.trace.TraceServerInterceptor;

import java.io.IOException;
import java.util.logging.Logger;

public class ReferenceServiceApp {
    private static final Logger LOGGER = Logger.getLogger(ReferenceServiceApp.class.getName());
    private static final int DEFAULT_PORT = 50051;

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        ReferenceDataService dataService = new ReferenceDataService();
        Server server = ServerBuilder.forPort(port)
                .addService(ServerInterceptors.intercept(
                        new ReferenceGrpcService(dataService),
                        new TraceServerInterceptor()))
                .build()
                .start();

        LOGGER.info("Reference Service started on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Stopping Reference Service");
                server.shutdown();
            }
        }));
        server.awaitTermination();
    }
}
