package ru.panov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.panov.exception.ApiError;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Обрабатывает исключение NotFoundException.
     *
     * @param ex исключение NotFoundException
     * @return ответ с ошибкой 404 Not Found
     */
    @ExceptionHandler(value
            = {NotFoundException.class})
    protected ResponseEntity<Object> notFoundError(NotFoundException ex) {
        ApiError apiError = new ApiError("Entity not found", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

    }

    /**
     * Обрабатывает исключение InputDataConflictException.
     *
     * @param ex исключение InputDataConflictException
     * @return ответ с ошибкой 409 Conflict
     */
    @ExceptionHandler(value
            = {InputDataConflictException.class})
    protected ResponseEntity<Object> InputDataError(InputDataConflictException ex) {
        ApiError apiError = new ApiError("Input data conflict", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Обрабатывает исключение IllegalArgumentException.
     *
     * @param ex исключение IllegalArgumentException
     * @return ответ с ошибкой 403 Forbidden
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> IllegalArgument(IllegalArgumentException ex) {
        ApiError apiError = new ApiError("Illegal argument exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }


    /**
     * Обрабатывает исключение ValidationException.
     *
     * @param ex исключение ValidationException
     * @return ответ с ошибкой 403 Forbidden
     */
    @ExceptionHandler(value
            = {ValidationException.class})
    protected ResponseEntity<Object> validationError(ValidationException ex) {
        ApiError apiError = new ApiError("ValidationException", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}