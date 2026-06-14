package org.example.fitness.jsonclient;

import org.example.fitness.exception.ValidationException;

import java.util.Scanner;

public class JsonClientConsole {
    private final JsonClientDumpHack jsonClientDumpHack;

    public JsonClientConsole(JsonClientDumpHack jsonClientDumpHack) {
        this.jsonClientDumpHack = jsonClientDumpHack;
    }

    public String readAndBuildJson(Scanner scanner) {
        System.out.println("JSON-дамп клиентов (copy-paste hack):");
        System.out.println("  1 — Все клиенты");
        System.out.println("  2 — Клиенты с активными абонементами");
        System.out.println("  3 — Клиенты без активных абонементов");
        System.out.println("  4 — Клиенты с абонементами по посещениям");
        System.out.println("  5 — Клиенты с абонементами по сроку");
        String option = scanner.nextLine().trim();

        String rawJson = switch (option) {
            case "1" -> jsonClientDumpHack.dumpAllClientsJson();
            case "2" -> jsonClientDumpHack.dumpActiveClientsJson();
            case "3" -> jsonClientDumpHack.dumpInactiveClientsJson();
            case "4" -> jsonClientDumpHack.dumpVisitBasedClientsJson();
            case "5" -> jsonClientDumpHack.dumpTimeBasedClientsJson();
            default -> throw new ValidationException("Некорректный режим JSON-вывода.");
        };

        return prettyPrintJson(rawJson);
    }

    private String prettyPrintJson(String json) {
        StringBuilder out = new StringBuilder();
        int indent = 0;
        boolean inString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                out.append(c);
                continue;
            }

            if (inString) {
                out.append(c);
                continue;
            }

            switch (c) {
                case '{', '[' -> {
                    out.append(c).append('\n');
                    indent++;
                    appendIndent(out, indent);
                }
                case '}', ']' -> {
                    out.append('\n');
                    indent--;
                    appendIndent(out, indent);
                    out.append(c);
                }
                case ',' -> {
                    out.append(c).append('\n');
                    appendIndent(out, indent);
                }
                case ':' -> out.append(": ");
                default -> {
                    if (!Character.isWhitespace(c)) {
                        out.append(c);
                    }
                }
            }
        }
        return out.toString();
    }

    private void appendIndent(StringBuilder out, int indent) {
        for (int i = 0; i < indent; i++) {
            out.append("  ");
        }
    }
}
