package ru.panov.service;

import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.domain.responseDTO.UserResponseDTO;

/**
 * Интерфейс UserService определяет методы для работы с пользователями в системе.
 */
public interface UserService {
    void register(UserRequestDTO dto);

    UserResponseDTO login(String username, String password);

    User getById(Long id);
}