package org.example.fitness.service.implementation;

import org.example.fitness.exception.ClientNotFoundException;
import org.example.fitness.model.Client;
import org.example.fitness.repository.IntClientRepository;
import org.example.fitness.service.IntClientService;
import org.example.fitness.validation.Validator;

import java.util.List;


public class ClientService implements IntClientService {

    private final IntClientRepository clientRepository;
    private final Validator<Client> validator;

    public ClientService(IntClientRepository clientRepository, Validator<Client> validator) {
        this.clientRepository = clientRepository;
        this.validator = validator;
    }

    @Override
    public Client register(Client client) {
        validator.validate(client);
        return clientRepository.save(client);
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
