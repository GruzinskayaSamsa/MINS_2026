package org.example.reference;

import io.grpc.stub.StreamObserver;
import org.example.reference.api.*;
import org.example.reference.form.TrainingFormInputParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(ReferenceServiceImpl.class);
    private final ReferenceCatalog catalog = new ReferenceCatalog();

    @Override
    public void validateClient(ValidateClientRequest request, StreamObserver<ValidateClientResponse> responseObserver) {
        withTrace(() -> {
            boolean exists = catalog.clientExists(request.getClientId());
            log.info("Проверка клиента: id={} существует={}", request.getClientId(), exists);
            responseObserver.onNext(ValidateClientResponse.newBuilder()
                    .setExists(exists)
                    .setMessage(exists ? "Клиент найден" : "Клиент не найден")
                    .build());
            responseObserver.onCompleted();
        });
    }

    @Override
    public void validateTraining(ValidateTrainingRequest request, StreamObserver<ValidateTrainingResponse> responseObserver) {
        withTrace(() -> {
            boolean exists = catalog.trainingExists(request.getTrainingId());
            log.info("Проверка тренировки: id={} существует={}", request.getTrainingId(), exists);
            responseObserver.onNext(ValidateTrainingResponse.newBuilder()
                    .setExists(exists)
                    .setMessage(exists ? "Тренировка найдена" : "Тренировка не найдена")
                    .build());
            responseObserver.onCompleted();
        });
    }

    @Override
    public void validateTrainingDraft(ValidateTrainingDraftRequest request,
                                    StreamObserver<ValidateTrainingDraftResponse> responseObserver) {
        withTrace(() -> {
            String error = catalog.validateTrainingDraft(
                    request.getName(), request.getDurationMinutes(), request.getMaxParticipants());
            boolean valid = error == null;
            log.info("Проверка черновика тренировки: допустимо={}", valid);
            responseObserver.onNext(ValidateTrainingDraftResponse.newBuilder()
                    .setValid(valid)
                    .setMessage(valid ? "" : error)
                    .build());
            responseObserver.onCompleted();
        });
    }

    @Override
    public void validateTrainingForm(ValidateTrainingFormRequest request,
                                     StreamObserver<ValidateTrainingFormResponse> responseObserver) {
        withTrace(() -> {
            TrainingFormInputParser.Result r = TrainingFormInputParser.parse(
                    request.getName(),
                    request.getDescription(),
                    request.getDurationMinutes(),
                    request.getMaxParticipants());
            if (r.valid()) {
                log.info("Форма типа тренировки принята: название={} длительностьМин={} максУчастников={}",
                        request.getName(), r.parsedDurationMinutes(), r.parsedMaxParticipants());
            } else {
                log.warn("Форма типа тренировки отклонена: название={} причина={}",
                        request.getName(), r.message());
            }
            responseObserver.onNext(ValidateTrainingFormResponse.newBuilder()
                    .setValid(r.valid())
                    .setMessage(r.valid() ? "" : r.message())
                    .setParsedDurationMinutes(r.parsedDurationMinutes())
                    .setParsedMaxParticipants(r.parsedMaxParticipants())
                    .build());
            responseObserver.onCompleted();
        });
    }

    @Override
    public void registerClient(RegisterClientRequest request, StreamObserver<RegisterClientResponse> responseObserver) {
        withTrace(() -> {
            catalog.registerClient(request.getClientId(), request.getName());
            log.info("Регистрация клиента: id={} имя={}", request.getClientId(), request.getName());
            responseObserver.onNext(RegisterClientResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        });
    }

    @Override
    public void registerTraining(RegisterTrainingRequest request, StreamObserver<RegisterTrainingResponse> responseObserver) {
        withTrace(() -> {
            catalog.registerTraining(request.getTrainingId(), request.getName());
            log.info("Регистрация тренировки: id={} название={}", request.getTrainingId(), request.getName());
            responseObserver.onNext(RegisterTrainingResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        });
    }

    private void withTrace(Runnable runnable) {
        String traceId = TraceKeys.safeTraceId(TraceKeys.TRACE_ID_CONTEXT.get());
        MDC.put("traceId", traceId);
        try {
            runnable.run();
        } finally {
            MDC.remove("traceId");
        }
    }
}
