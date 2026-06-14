package org.example.fitness.reference;

public interface ClientReferenceGateway {
    boolean clientExists(int clientId);

    default void registerClient(int clientId, String name) {
    }
}
