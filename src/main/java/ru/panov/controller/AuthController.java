package ru.panov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.service.UserService;

/**
 * Контроллер работы регистрацией пользователя.
 */
@RestController
@AllArgsConstructor
@Tag(name = "Auth Controller", description = "Контроллер регистрации пользователя")
public class AuthController {
    private final UserService userService;
    public static final String REGISTRATION_PATH = "/registration";

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param userRequestDTO данные нового пользователя
     * @return ответ о успешном создании пользователя
     */
    @PostMapping(value = REGISTRATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<Void> registrationUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        userService.register(userRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}