package org.example.fitness.repository.implementation;

import org.example.fitness.model.Client;
import org.example.fitness.repository.IdGenerator;
import org.example.fitness.repository.IntClientRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClientRepository implements IntClientRepository {
    private final Map<Integer, Client> stor = new ConcurrentHashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Client save(Client client) {
        if (client.getId() == 0) {
            client.setId(idGenerator.next());
        }
        stor.put(client.getId(), copy(client));
        return client;
    }

    @Override
    public Client findById(int id) {
        return copy(stor.get(id));
    }

    @Override
    public List<Client> findAll() {
        return stor.values().stream().map(ClientRepository::copy).collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        stor.remove(id);
    }

    private static Client copy(Client client) {
        if (client == null) return null;
        return new Client(client.getId(), client.getName(), client.getPhone(), client.getEmail());
    }
}

