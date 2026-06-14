package org.example.fitness.service;

import org.example.fitness.model.Client;

import java.util.List;

public interface IntClientService {

    Client register(Client client);

    Client getById(int id);

    List<Client> getAll();

    void delete(int id);
}
