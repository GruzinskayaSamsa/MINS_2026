package org.example.fitness.reference;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.reference.api.ReferenceServiceGrpc;
import org.example.reference.api.RegisterClientRequest;
import org.example.reference.api.ValidateClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GrpcClientReferenceGateway implements ClientReferenceGateway, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(GrpcClientReferenceGateway.class);
    private final ManagedChannel channel; // Канал связи с сервисом
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;

    public GrpcClientReferenceGateway(String host, int port) { // Конструктор для создания канала связи с сервисом
        this.channel = ManagedChannelBuilder.forAddress(host, port) // Создаем канал связи с сервисом
                .usePlaintext() // Используем plaintext для простого текста 
                .intercept(new TraceIdClientInterceptor()) // Интерсептор для добавления traceId в запрос
                .build(); // Создаем канал связи с сервисом
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel); // Создаем блок для вызова методов сервиса
    }

    @Override
    public boolean clientExists(int clientId) {
        try {
            return stub.withDeadlineAfter(2, TimeUnit.SECONDS) // Устанавливаем дедлайн для вызова метода
                    .validateClient(ValidateClientRequest.newBuilder().setClientId(clientId).build()) // Создаем запрос для валидации клиента
                    .getExists(); // Получаем результат валидации
        } catch (StatusRuntimeException e) {
            throw toUnavailable(e); // Обрабатываем ошибку соединения
        }
    }

    @Override
    public void registerClient(int clientId, String name) {
        try {
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .registerClient(RegisterClientRequest.newBuilder()
                            .setClientId(clientId)
                            .setName(name)
                            .build());
            log.info("Клиент зарегистрирован в справочном сервисе, id={}", clientId);
        } catch (StatusRuntimeException e) {
            throw toUnavailable(e);
        }
    }

    @Override
    public void close() { // Закрываем канал связи с сервисом
        channel.shutdown();
    }

    private ReferenceServiceUnavailableException toUnavailable(StatusRuntimeException e) { // Обрабатываем ошибку соединения
        Status.Code code = e.getStatus().getCode();
        log.warn("Вызов справочного сервиса не удался, статус={}", code);
        if (code == Status.Code.UNAVAILABLE || code == Status.Code.DEADLINE_EXCEEDED) {
            return new ReferenceServiceUnavailableException(
                    "Справочный сервис временно недоступен. Повторите попытку позже.");
        }
        return new ReferenceServiceUnavailableException("Ошибка связи со справочным сервисом: " + code);
    }
}
