package org.example.fitness.service.implementation;

import org.example.fitness.exception.ClientNotFoundException;
import org.example.fitness.model.Client;
import org.example.fitness.reference.ClientReferenceGateway;
import org.example.fitness.repository.IntClientRepository;
import org.example.fitness.service.IntClientService;
import org.example.fitness.validation.Validator;

import java.util.List;


public class ClientService implements IntClientService {

    private final IntClientRepository clientRepository;
    private final ClientReferenceGateway clientReferenceGateway;
    private final Validator<Client> validator;

    public ClientService(IntClientRepository clientRepository,
                         ClientReferenceGateway clientReferenceGateway,
                         Validator<Client> validator) {
        this.clientRepository = clientRepository;
        this.clientReferenceGateway = clientReferenceGateway;
        this.validator = validator;
    }

    @Override
    public Client register(Client client) {
        validator.validate(client);
        Client saved = clientRepository.save(client);
        clientReferenceGateway.registerClient(saved.getId(), saved.getName());
        return saved;
    }

    @Override
    public Client getById(int id) {
        Client client = clientRepository.findById(id);
        if (client == null) {
            throw new ClientNotFoundException(id);
        }
        return client;
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public void delete(int id) {
        if (clientRepository.findById(id) == null) {
            throw new ClientNotFoundException(id);
        }
        clientRepository.deleteById(id);
    }
}
