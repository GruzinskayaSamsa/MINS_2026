package org.example.fitness.repository;

//Генерация уникальных id. Вынесено в отдельный класс (DRY, S - одна ответственность).

public final class IdGenerator {

    private int next = 1;

    public int next() {
        return next++;
    }

    public void reset() {
        next = 1;
    }
}
