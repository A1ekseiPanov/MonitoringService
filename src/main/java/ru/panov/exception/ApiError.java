package ru.panov.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Класс для представления сообщения об ошибке API.
 */
@Getter
@Setter
public class ApiError {
    private String message;
    private List<String> debugMessage;

    public ApiError(String message, List<String> debugMessage) {
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ApiError(String message, String debugMessage) {

        this.message = message;
        this.debugMessage = List.of(debugMessage);
    }
}