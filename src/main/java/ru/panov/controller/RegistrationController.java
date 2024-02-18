package ru.panov.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.service.UserService;

import static ru.panov.controller.RegistrationController.REGISTRATION_PATH;

/**
 * Контроллер для регистрации новых пользователей.
 */
@RestController
@AllArgsConstructor
@RequestMapping(REGISTRATION_PATH)
public class RegistrationController {
    private final UserService userService;
    public static final String REGISTRATION_PATH = "/registration";

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param userRequestDTO данные нового пользователя
     * @return ответ о успешном создании пользователя
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationUser(@RequestBody UserRequestDTO userRequestDTO) {
        userService.register(userRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}