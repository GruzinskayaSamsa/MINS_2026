package delivery.client;

import delivery.grpc.CalculateTimeRequest;
import delivery.grpc.CalculateTimeResponse;
import delivery.grpc.GetZoneBasePriceRequest;
import delivery.grpc.GetZoneBasePriceResponse;
import delivery.grpc.ReferenceServiceGrpc;
import delivery.grpc.ValidateZoneRequest;
import delivery.grpc.ValidateZoneResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Клиент Service A к справочному Service B по gRPC.
 */
public class ReferenceClient implements AutoCloseable {

    private static final int DEFAULT_DEADLINE_MS = 3_000;

    private final ManagedChannel channel;

    public ReferenceClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    // Дедлайн задаем для каждого запроса отдельно.
    private ReferenceServiceGrpc.ReferenceServiceBlockingStub stubWithDeadline() {
        return ReferenceServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(DEFAULT_DEADLINE_MS, TimeUnit.MILLISECONDS);
    }

    public Optional<ValidateZoneResponse> validateZone(String zone, String traceId) {
        ValidateZoneRequest request = ValidateZoneRequest.newBuilder()
                .setZone(zone)
                .setTraceId(traceId)
                .build();
        try {
            return Optional.of(stubWithDeadline().validateZone(request));
        } catch (StatusRuntimeException e) {
            logGrpcError("validateZone", traceId, e);
            return Optional.empty();
        }
    }

    public Optional<CalculateTimeResponse> calculateDeliveryTime(String zone, String traceId) {
        CalculateTimeRequest request = CalculateTimeRequest.newBuilder()
                .setZone(zone)
                .setTraceId(traceId)
                .build();
        try {
            return Optional.of(stubWithDeadline().calculateDeliveryTime(request));
        } catch (StatusRuntimeException e) {
            logGrpcError("calculateDeliveryTime", traceId, e);
            return Optional.empty();
        }
    }

    public Optional<GetZoneBasePriceResponse> getZoneBasePrice(String zone, String traceId) {
        GetZoneBasePriceRequest request = GetZoneBasePriceRequest.newBuilder()
                .setZone(zone)
                .setTraceId(traceId)
                .build();
        try {
            return Optional.of(stubWithDeadline().getZoneBasePrice(request));
        } catch (StatusRuntimeException e) {
            logGrpcError("getZoneBasePrice", traceId, e);
            return Optional.empty();
        }
    }

    private static void logGrpcError(String rpc, String traceId, StatusRuntimeException e) {
        Status.Code code = e.getStatus().getCode();
        String description = e.getStatus().getDescription();
        if (description == null || description.isEmpty()) {
            description = "без описания";
        }

        if (code == Status.Code.UNAVAILABLE) {
            System.out.println("[Core] [INFO] gRPC " + rpc
                    + ": Service B недоступен (не запущен или порт 9090 занят). traceId=" + traceId
                    + ", status=" + code + ": " + description);
            return;
        }

        if (code == Status.Code.DEADLINE_EXCEEDED) {
            System.out.println("[Core] [INFO] gRPC " + rpc
                    + ": превышен таймаут ответа (" + DEFAULT_DEADLINE_MS + " мс). traceId=" + traceId
                    + ", status=" + code + ": " + description);
            return;
        }

        if (code == Status.Code.INTERNAL) {
            System.out.println("[Core] [INFO] gRPC " + rpc
                    + ": внутренняя ошибка на стороне Service B. traceId=" + traceId
                    + ", status=" + code + ": " + description);
            return;
        }

        System.out.println("[Core] gRPC " + rpc + " ошибка. traceId=" + traceId
                + ", status=" + code + ": " + description);
    }

    @Override
    public void close() throws InterruptedException {
        channel.shutdown();
        if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
            channel.shutdownNow();
        }
    }
}
