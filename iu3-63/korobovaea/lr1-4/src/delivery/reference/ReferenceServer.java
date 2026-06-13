package delivery.reference;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ReferenceServer {

    public static void main(String[] args) throws Exception {

        Server server = ServerBuilder.forPort(9090)
                .addService(new ReferenceServiceImpl())
                .build();

        server.start();

        System.out.println("Service B (Reference) gRPC на порту 9090. В логах RPC используется traceId из запроса.");

        server.awaitTermination();
    }
}