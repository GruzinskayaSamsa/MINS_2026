package org.example.fitness.validation;

import org.example.fitness.exception.ValidationException;

/**
 * Контракт валидации. Новые правила — новые реализации, без изменения существующего кода (O - Open/Closed).
 */
public interface Validator<T> {

    void validate(T entity) throws ValidationException;
}
