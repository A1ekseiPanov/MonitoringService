package ru.panov.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.panov.exception.ApiError;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

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
     * Обрабатывает исключение AccessDeniedException.
     *
     * @return Ответ с информацией об ошибке и статусом FORBIDDEN (403)
     */
    @ExceptionHandler(value
            = {AccessDeniedException.class})
    protected ResponseEntity<Object> validationError() {
        ApiError apiError = new ApiError("AccessDeniedException", "Неподходящая роль пользователя");
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errorMap, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiError apiError = new ApiError("HandleExceptionInternal", "Ошибка сервера");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}