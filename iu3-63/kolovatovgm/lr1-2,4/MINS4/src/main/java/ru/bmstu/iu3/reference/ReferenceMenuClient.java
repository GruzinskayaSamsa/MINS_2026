package ru.bmstu.iu3.reference;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import ru.bmstu.iu3.exception.ReferenceServiceUnavailableException;
import ru.bmstu.iu3.exception.ValidationException;
import ru.bmstu.iu3.grpc.DishMessage;
import ru.bmstu.iu3.grpc.DishRequest;
import ru.bmstu.iu3.grpc.DishResponse;
import ru.bmstu.iu3.grpc.MenuRequest;
import ru.bmstu.iu3.grpc.MenuResponse;
import ru.bmstu.iu3.grpc.ReferenceServiceGrpc;
import ru.bmstu.iu3.menu.Dish;
import ru.bmstu.iu3.menu.MenuRepository;
import ru.bmstu.iu3.menu.SimpleDish;
import ru.bmstu.iu3.trace.TraceMetadata;

import java.io.Closeable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ReferenceMenuClient implements MenuRepository, Closeable {
    private static final Logger LOGGER = Logger.getLogger(ReferenceMenuClient.class.getName());

    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;

    public ReferenceMenuClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void showMenu() {
        MenuResponse response = withFailuresHandled(new GrpcCall<MenuResponse>() {
            @Override
            public MenuResponse execute(String traceId) {
                return tracedStub(traceId).getMenu(MenuRequest.newBuilder().build());
            }
        }, "ReferenceService.GetMenu");

        for (DishMessage dish : response.getDishesList()) {
            System.out.println(dish.getNumber() + ". " + dish.getName() + " --------------------------------- "
                    + dish.getPrice() + " руб.\n Описание: " + dish.getDescription());
        }
    }

    @Override
    public Dish getDishByNumber(final int number) {
        DishResponse response = withFailuresHandled(new GrpcCall<DishResponse>() {
            @Override
            public DishResponse execute(String traceId) {
                return tracedStub(traceId).getDishByNumber(DishRequest.newBuilder()
                        .setNumber(number)
                        .build());
            }
        }, "ReferenceService.GetDishByNumber");
        DishMessage dish = response.getDish();
        return new SimpleDish(dish.getName(), dish.getPrice(), dish.getDescription());
    }

    @Override
    public void addDish(Dish dish) {
        throw new UnsupportedOperationException("Core Service не изменяет справочник меню.");
    }

    @Override
    public void removeDish(Dish dish) {
        throw new UnsupportedOperationException("Core Service не изменяет справочник меню.");
    }

    @Override
    public void close() {
        channel.shutdown();
        try {
            if (!channel.awaitTermination(3, TimeUnit.SECONDS)) {
                channel.shutdownNow();
            }
        } catch (InterruptedException e) {
            channel.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private ReferenceServiceGrpc.ReferenceServiceBlockingStub tracedStub(String traceId) {
        Metadata metadata = new Metadata();
        metadata.put(TraceMetadata.TRACE_ID_KEY, traceId);
        return stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
    }

    private <T> T withFailuresHandled(GrpcCall<T> call, String operationName) {
        String traceId = UUID.randomUUID().toString();
        LOGGER.info("traceId=" + traceId + " CoreService -> " + operationName);
        try {
            return call.execute(traceId);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                throw new ValidationException(e.getStatus().getDescription());
            }
            LOGGER.warning("traceId=" + traceId + " Reference Service unavailable: " + e.getStatus());
            throw new ReferenceServiceUnavailableException(
                    "Сервис справочников временно недоступен. Запустите Reference Service и повторите действие.");
        }
    }

    private interface GrpcCall<T> {
        T execute(String traceId);
    }
}
