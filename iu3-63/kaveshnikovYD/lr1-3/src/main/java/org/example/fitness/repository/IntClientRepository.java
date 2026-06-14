package org.example.fitness.repository;

import org.example.fitness.model.Client;

import java.util.List;

public interface IntClientRepository {
    Client save(Client client);
    Client findById(int id);
    List<Client> findAll();
    void deleteById(int id);
}

