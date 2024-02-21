package ru.panov.exception;

/**
 * Исключение, выбрасываемое при отсутствии запрашиваемых данных или ресурсов.
 * Это исключение возникает, когда запрашиваемые данные не найдены
 * или когда запрашиваемый ресурс отсутствует.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}