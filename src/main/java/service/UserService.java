package service;

import entity.User;

import exception.InputDataConflictException;
import exception.NotFoundException;
import repository.UserRepository;
import repository.memory.MemoryUserRepository;
import util.AuditLog;

/**
 * Класс UserService предоставляет методы для работы с пользователями в системе.
 * Он обеспечивает функции регистрации, входа и выхода пользователя.
 */
public class UserService {
    private User registeredUser;
    private final UserRepository userRepository = MemoryUserRepository.getInstance();
    private static UserService INSTANCE = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public static void resetInstance() {
        INSTANCE = new UserService();
    }


    /**
     * Создает нового пользователя с указанным именем пользователя и паролем.
     *
     * @param username Имя пользователя
     * @param password Пароль
     * @throws InputDataConflictException если пользователь с таким именем уже существует
     */
    public void register(String username, String password) {
        User currentUser = userRepository.findByUsername(username);
        if (currentUser != null && currentUser.getPassword().equals(password)) {
            throw new InputDataConflictException("Такой пользователь уже существует");
        } else {
            userRepository.save(new User(username, password));
            AuditLog.logAction("Пользователь username(" + username + ") успешно зарегистрировался");
        }
    }

    /**
     * Позволяет пользователю войти в систему с указанным именем пользователя и паролем.
     *
     * @param username Имя пользователя
     * @param password Пароль
     * @throws InputDataConflictException если пользователь уже вошел в систему
     * @throws IllegalArgumentException   если указаны неверное имя пользователя или пароль
     */
    public void login(String username, String password) {
        User currentUser = userRepository.findByUsername(username);
        User loggedUser = getLoggedUser();
        if (loggedUser != null && loggedUser.getUsername().equals(username) && loggedUser.getPassword().equals(password)) {
            throw new InputDataConflictException("Вы уже выполнили вход");
        } else if (loggedUser != null) {
            throw new InputDataConflictException("Нельзя войти пока есть залогиненый пользователь: username(" + getLoggedUser().getUsername() + ")");
        }
        if (currentUser != null && currentUser.getPassword().equals(password)) {
            registeredUser = currentUser;
            AuditLog.logAction("Пользователь username(" + username + ") вошел в систему");
        } else {
            throw new IllegalArgumentException("Неверное имя пользователя или пароль. Ошибка входа.");
        }
    }

    /**
     * Выполняет выход из системы для текущего пользователя.
     *
     * @throws NotFoundException если пользователь не вошел в систему
     */
    public void logout() {
        if (registeredUser != null) {
            AuditLog.logAction("Пользователь " + registeredUser.getUsername() + " вышел из системы успешно");
            registeredUser = null;
        } else {
            throw new NotFoundException("В данный момент ни один пользователь не вошел в систему.");
        }
    }

    /**
     * Возвращает текущего залогиненного пользователя.
     *
     * @return Залогиненный пользователь или null, если никто не вошел в систему
     */
    public User getLoggedUser() {
        return registeredUser;
    }
}