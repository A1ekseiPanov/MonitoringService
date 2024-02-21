package ru.panov.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов выхода пользователя из системы.
 */
@RestController
public class LogoutController {

    /**
     * Метод для выполнения выхода пользователя.
     *
     * @param session сессия пользователя
     * @return ответ о успешном выходе пользователя из системы
     */
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return ResponseEntity.ok("Logged out successfully");
    }
}