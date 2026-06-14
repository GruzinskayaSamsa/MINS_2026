package org.example.reference;

import org.example.reference.form.TrainingFormInputParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReferenceCatalog {
    private final Map<Integer, String> clients = new ConcurrentHashMap<>();
    private final Map<Integer, String> trainings = new ConcurrentHashMap<>();

    public void registerClient(int id, String name) {
        clients.put(id, name);
    }

    public void registerTraining(int id, String name) {
        trainings.put(id, name);
    }

    public boolean clientExists(int clientId) {
        return clients.containsKey(clientId);
    }

    public boolean trainingExists(int trainingId) {
        return trainings.containsKey(trainingId);
    }

    /**
     * Те же правила, что {@link org.example.reference.form.TrainingFormInputParser} для целых полей.
     */
    public String validateTrainingDraft(String name, int durationMinutes, int maxParticipants) {
        TrainingFormInputParser.Result r = TrainingFormInputParser.parse(
                name, "", String.valueOf(durationMinutes), String.valueOf(maxParticipants));
        return r.valid() ? null : r.message();
    }
}
