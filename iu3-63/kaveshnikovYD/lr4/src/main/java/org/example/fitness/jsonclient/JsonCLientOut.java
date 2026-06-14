package org.example.fitness.jsonclient;

import org.example.fitness.model.Client;
import org.example.fitness.model.Subscription;
import org.example.fitness.service.IntClientService;
import org.example.fitness.service.IntSubscriptionService;

import java.util.List;

public class JsonCLientOut implements IntJsonCLientOut {
    private final IntClientService clientService;
    private final IntSubscriptionService subscriptionService;

    public JsonCLientOut(IntClientService clientService, IntSubscriptionService subscriptionService) {
        this.clientService = clientService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String allClientsJson() {
        List<Client> clients = clientService.getAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Client client : clients) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("{")
                    .append("\"id\":").append(client.getId()).append(",")
                    .append("\"name\":\"").append(escape(client.getName())).append("\",")
                    .append("\"phone\":\"").append(escape(client.getPhone())).append("\",")
                    .append("\"email\":\"").append(escape(client.getEmail())).append("\"")
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String activeClientsJson() {
        List<Client> clients = clientService.getAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Client client : clients) {
            List<Subscription> subscriptions = subscriptionService.getByClientId(client.getId());
            boolean hasActive = false;
            for (Subscription subscription : subscriptions) {
                if (subscription.isActive()) {
                    hasActive = true;
                    break;
                }
            }
            if (!hasActive) {
                continue;
            }
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("{")
                    .append("\"id\":").append(client.getId()).append(",")
                    .append("\"name\":\"").append(escape(client.getName())).append("\",")
                    .append("\"phone\":\"").append(escape(client.getPhone())).append("\",")
                    .append("\"email\":\"").append(escape(client.getEmail())).append("\"")
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String inactiveClientsJson() {
        List<Client> clients = clientService.getAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Client client : clients) {
            List<Subscription> subscriptions = subscriptionService.getByClientId(client.getId());
            boolean hasActive = false;
            for (Subscription subscription : subscriptions) {
                if (subscription.isActive()) {
                    hasActive = true;
                    break;
                }
            }
            if (hasActive) {
                continue;
            }
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("{")
                    .append("\"id\":").append(client.getId()).append(",")
                    .append("\"name\":\"").append(escape(client.getName())).append("\",")
                    .append("\"phone\":\"").append(escape(client.getPhone())).append("\",")
                    .append("\"email\":\"").append(escape(client.getEmail())).append("\"")
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String visitBasedClientsJson() {
        List<Client> clients = clientService.getAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Client client : clients) {
            List<Subscription> subscriptions = subscriptionService.getByClientId(client.getId());
            boolean hasVisitBased = false;
            for (Subscription subscription : subscriptions) {
                if (subscription.isVisitBased()) {
                    hasVisitBased = true;
                    break;
                }
            }
            if (!hasVisitBased) {
                continue;
            }
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("{")
                    .append("\"id\":").append(client.getId()).append(",")
                    .append("\"name\":\"").append(escape(client.getName())).append("\",")
                    .append("\"phone\":\"").append(escape(client.getPhone())).append("\",")
                    .append("\"email\":\"").append(escape(client.getEmail())).append("\"")
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String timeBasedClientsJson() {
        List<Client> clients = clientService.getAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Client client : clients) {
            List<Subscription> subscriptions = subscriptionService.getByClientId(client.getId());
            boolean hasTimeBased = false;
            for (Subscription subscription : subscriptions) {
                if (!subscription.isVisitBased()) {
                    hasTimeBased = true;
                    break;
                }
            }
            if (!hasTimeBased) {
                continue;
            }
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("{")
                    .append("\"id\":").append(client.getId()).append(",")
                    .append("\"name\":\"").append(escape(client.getName())).append("\",")
                    .append("\"phone\":\"").append(escape(client.getPhone())).append("\",")
                    .append("\"email\":\"").append(escape(client.getEmail())).append("\"")
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
