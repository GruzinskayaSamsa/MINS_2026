package org.example.fitness.jsonclient;

import org.example.fitness.model.Client;
import org.example.fitness.model.Subscription;
import org.example.fitness.service.IntClientService;
import org.example.fitness.service.IntSubscriptionService;

import java.util.List;

/**
 * Намеренно "грязный" модуль для демонстрации Copy-Paste Programming.
 * Один и тот же код JSON-сборки повторяется в нескольких методах.
 */
public class JsonClientDumpHack {
    private final IntClientService clientService;
    private final IntSubscriptionService subscriptionService;

    public JsonClientDumpHack(IntClientService clientService, IntSubscriptionService subscriptionService) {
        this.clientService = clientService;
        this.subscriptionService = subscriptionService;
    }

    public String dumpAllClientsJson() {
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

    public String dumpActiveClientsJson() {
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

    public String dumpInactiveClientsJson() {
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

    public String dumpVisitBasedClientsJson() {
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

    public String dumpTimeBasedClientsJson() {
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
