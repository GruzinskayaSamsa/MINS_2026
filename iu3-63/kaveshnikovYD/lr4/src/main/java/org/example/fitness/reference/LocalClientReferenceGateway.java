package org.example.fitness.reference;

import org.example.fitness.repository.IntClientRepository;

public class LocalClientReferenceGateway implements ClientReferenceGateway {
    private final IntClientRepository clientRepository;

    public LocalClientReferenceGateway(IntClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean clientExists(int clientId) {
        return clientRepository.findById(clientId) != null;
    }
}
