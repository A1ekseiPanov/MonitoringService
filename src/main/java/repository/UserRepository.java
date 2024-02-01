package repository;

import entity.User;

import java.util.Optional;

/**
 * Интерфейс UserRepository определяет методы для работы с пользователями в системе.
 */
public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User save(User user);

    User update(Long id, User user);
}