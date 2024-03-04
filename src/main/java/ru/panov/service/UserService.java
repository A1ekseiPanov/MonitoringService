package ru.panov.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.domain.responseDTO.UserResponseDTO;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Интерфейс UserService определяет методы для работы с пользователями в системе.
 */
public interface UserService extends UserDetailsService {
    void register(UserRequestDTO dto);

    User getById(Long id);
}