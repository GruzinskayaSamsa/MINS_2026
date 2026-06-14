package org.example.fitness.reference;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Training;
import org.example.reference.api.ReferenceServiceGrpc;
import org.example.reference.api.RegisterTrainingRequest;
import org.example.reference.api.ValidateTrainingDraftRequest;
import org.example.reference.api.ValidateTrainingFormRequest;
import org.example.reference.api.ValidateTrainingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GrpcTrainingReferenceGateway implements TrainingReferenceGateway, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(GrpcTrainingReferenceGateway.class);
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;

    public GrpcTrainingReferenceGateway(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .intercept(new TraceIdClientInterceptor())
                .build();
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public boolean trainingExists(int trainingId) {
        try {
            return stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .validateTraining(ValidateTrainingRequest.newBuilder().setTrainingId(trainingId).build())
                    .getExists();
        } catch (StatusRuntimeException e) {
            throw toUnavailable(e);
        }
    }

    @Override
    public ParsedTrainingFormValues parseAndValidateTrainingForm(String name, String desc, String durationStr, String maxStr) {
        try {
            var resp = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .validateTrainingForm(ValidateTrainingFormRequest.newBuilder()
                            .setName(name == null ? "" : name)
                            .setDescription(desc == null ? "" : desc)
                            .setDurationMinutes(durationStr == null ? "" : durationStr)
                            .setMaxParticipants(maxStr == null ? "" : maxStr)
                            .build());
            if (!resp.getValid()) {
                String msg = resp.getMessage();
                throw new ValidationException(
                        msg == null || msg.isBlank() ? "Справочник отклонил данные типа тренировки." : msg);
            }
            return new ParsedTrainingFormValues(resp.getParsedDurationMinutes(), resp.getParsedMaxParticipants());
        } catch (StatusRuntimeException e) {
            throw toUnavailable(e);
        }
    }

    @Override
    public void validateTrainingDraftAgainstReference(Training training) {
        try {
            int minutes = Math.toIntExact(training.getDuration().toMinutes());
            var response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .validateTrainingDraft(ValidateTrainingDraftRequest.newBuilder()
                            .setName(training.getName() == null ? "" : training.getName())
                            .setDurationMinutes(minutes)
                            .setMaxParticipants(training.getMaxParticipants())
                            .build());
            if (!response.getValid()) {
                String msg = response.getMessage();
                throw new ValidationException(
                        msg == null || msg.isBlank() ? "Справочник отклонил данные типа тренировки." : msg);
            }
        } catch (ArithmeticException e) {
            throw new ValidationException("Слишком большая длительность для проверки в справочнике.");
        } catch (StatusRuntimeException e) {
            throw toUnavailable(e);
        }
    }

    @Override
    public void registerTraining(int trainingId, String name) {
        try {
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .registerTraining(RegisterTrainingRequest.newBuilder()
                            .setTrainingId(trainingId)
                            .setName(name)
                            .build());
            log.info("Тип тренировки зарегистрирован в справочном сервисе, id={}", trainingId);
        } catch (StatusRuntimeException e) {
            throw toUnavailable(e);
        }
    }

    @Override
    public void close() {
        channel.shutdown();
    }

    private ReferenceServiceUnavailableException toUnavailable(StatusRuntimeException e) {
        Status.Code code = e.getStatus().getCode();
        log.warn("Вызов справочного сервиса не удался, статус={}", code);
        if (code == Status.Code.UNAVAILABLE || code == Status.Code.DEADLINE_EXCEEDED) {
            return new ReferenceServiceUnavailableException(
                    "Справочный сервис временно недоступен. Повторите попытку позже.");
        }
        return new ReferenceServiceUnavailableException("Ошибка связи со справочным сервисом: " + code);
    }
}
