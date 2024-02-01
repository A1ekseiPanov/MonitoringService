package repository.memory;

import entity.Role;
import entity.User;
import exception.NotFoundException;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс MemoryUserRepository представляет реализацию интерфейса UserRepository
 * для работы с пользователями в памяти.
 */
public class MemoryUserRepository implements UserRepository {
    private static MemoryUserRepository INSTANCE = new MemoryUserRepository();
    private Long id = 0L;
    private List<User> users = new ArrayList<>();

    private MemoryUserRepository() {
    }

    public static MemoryUserRepository getInstance() {
        return INSTANCE;
    }


    {
        User admin = new User("admin", "admin");
        admin.setRole(Role.ADMIN.toString());
        users.add(admin);
    }

    /**
     * Поиск пользователя по его имени.
     *
     * @param username Имя пользователя для поиска
     * @return Найденный пользователь или null, если пользователь не найден
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    /**
     * Сохранение пользователя.
     *
     * @param user Пользователь для сохранения
     * @return Сохраненный пользователь
     */
    @Override
    public User save(User user) {
        if (user.getId() != null) {
            return update(user.getId(), user);
        }
        if (user.getId() == null) {
            user.setId(++id);
        }
        users.add(user);
        return user;
    }

    /**
     * Поиск пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя для поиска
     * @return Найденный пользователь или null, если пользователь не найден
     */
    @Override
    public Optional<User> findById(Long id) {
        return users.stream().filter(u -> Objects.equals(u.getId(), id)).findFirst();
    }

    /**
     * Обновление информации о пользователе.
     *
     * @param id          Идентификатор пользователя для обновления
     * @param updatedUser Обновленная информация о пользователе
     * @return Обновленный пользователь
     */
    @Override
    public User update(Long id, User updatedUser) {
        Optional<User> user = findById(id);
        if (user.isPresent()) {
            updatedUser.setId(user.get().getId());
            updatedUser.setMeterReadings(user.get().getMeterReadings());
            int index = users.indexOf(user.get());
            users.set(index, updatedUser);
            return updatedUser;
        } else {
            throw new NotFoundException(String.format("Пользователь с id: %s не найден", id));
        }
    }
}