package repository;

import entity.User;

/**
 * Интерфейс UserRepository определяет методы для работы с пользователями в системе.
 */
public interface UserRepository {
    User findById(Long id);

    User findByUsername(String username);

    User save(User user);

    User update(Long id, User user);
}