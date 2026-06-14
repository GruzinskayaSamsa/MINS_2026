package ru.bmstu.iu3.reference;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ru.bmstu.iu3.exception.ValidationException;
import ru.bmstu.iu3.grpc.DishMessage;
import ru.bmstu.iu3.grpc.DishRequest;
import ru.bmstu.iu3.grpc.DishResponse;
import ru.bmstu.iu3.grpc.MenuRequest;
import ru.bmstu.iu3.grpc.MenuResponse;
import ru.bmstu.iu3.grpc.ReferenceServiceGrpc;
import ru.bmstu.iu3.menu.Dish;
import ru.bmstu.iu3.trace.TraceContext;

import java.util.List;
import java.util.logging.Logger;

public class ReferenceGrpcService extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger LOGGER = Logger.getLogger(ReferenceGrpcService.class.getName());

    private final ReferenceDataService dataService;

    public ReferenceGrpcService(ReferenceDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void getMenu(MenuRequest request, StreamObserver<MenuResponse> responseObserver) {
        LOGGER.info("traceId=" + TraceContext.currentTraceId() + " ReferenceService.GetMenu");
        List<Dish> dishes = dataService.getDishes();
        MenuResponse.Builder response = MenuResponse.newBuilder();
        for (int i = 0; i < dishes.size(); i++) {
            response.addDishes(toMessage(i + 1, dishes.get(i)));
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getDishByNumber(DishRequest request, StreamObserver<DishResponse> responseObserver) {
        LOGGER.info("traceId=" + TraceContext.currentTraceId()
                + " ReferenceService.GetDishByNumber number=" + request.getNumber());
        try {
            Dish dish = dataService.getDishByNumber(request.getNumber());
            responseObserver.onNext(DishResponse.newBuilder()
                    .setDish(toMessage(request.getNumber(), dish))
                    .build());
            responseObserver.onCompleted();
        } catch (ValidationException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    private static DishMessage toMessage(int number, Dish dish) {
        return DishMessage.newBuilder()
                .setNumber(number)
                .setName(dish.getName())
                .setPrice(dish.getPrice())
                .setDescription(dish.getDescription())
                .build();
    }
}
