package ru.panov.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.domain.responseDTO.UserResponseDTO;
import ru.panov.service.UserService;

import static ru.panov.controller.LoginController.LOGIN_PATH;

/**
 * Контроллер для обработки запросов аутентификации пользователей.
 */
@RestController
@AllArgsConstructor
@RequestMapping(LOGIN_PATH)
public class LoginController {
    private final UserService userService;
    public static final String LOGIN_PATH = "/login";

    /**
     * Метод для выполнения входа пользователя.
     *
     * @param dto     запрос пользователя для аутентификации
     * @param session сессия пользователя
     * @return ответ с данными пользователя после аутентификации
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO dto, HttpSession session) {
        UserResponseDTO user = userService.login(dto.getUsername(), dto.getPassword());
        session.setAttribute("user", user);
        return ResponseEntity.ok(user);
    }
}