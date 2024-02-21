package ru.panov.exception;

import lombok.Getter;

/**
 * Исключение, выбрасываемое при ошибке валидации.
 */
@Getter
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
