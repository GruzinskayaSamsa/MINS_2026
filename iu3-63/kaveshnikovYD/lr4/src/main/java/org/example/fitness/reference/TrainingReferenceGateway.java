package org.example.fitness.reference;

import org.example.fitness.model.Training;

public interface TrainingReferenceGateway {
    boolean trainingExists(int trainingId);

    /**
     * Проверка формы «новый тип тренировки»: справочник по gRPC или те же правила локально.
     * Всегда вызывается при добавлении типа из консоли — запрос доходит до Reference при не-gRPC недоступности только в grpc-режиме.
     */
    ParsedTrainingFormValues parseAndValidateTrainingForm(String name, String desc, String durationStr, String maxStr);

    /**
     * Дополнительная проверка черновика в справочнике по gRPC. В режиме local — заглушка.
     */
    default void validateTrainingDraftAgainstReference(Training training) {
    }

    default void registerTraining(int trainingId, String name) {
    }
}
