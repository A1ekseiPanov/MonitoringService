package service;

import entity.User;

import exception.InputDataConflictException;
import exception.NotFoundException;
import repository.UserRepository;
import repository.memory.MemoryUserRepository;
import util.AuditLog;

import java.util.Optional;

/**
 * Класс UserService предоставляет методы для работы с пользователями в системе.
 * Он обеспечивает функции регистрации, входа и выхода пользователя.
 */
public class UserService {
    private User registeredUser;
    private UserRepository userRepository;
    private static UserService INSTANCE = new UserService();

    private UserService() {
        this.userRepository = MemoryUserRepository.getInstance();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    /**
     * Создает нового пользователя с указанным именем пользователя и паролем.
     *
     * @param username Имя пользователя
     * @param password Пароль
     * @throws InputDataConflictException если пользователь с таким именем уже существует
     */
    public void register(String username, String password) {
        Optional<User> currentUser = userRepository.findByUsername(username);
        if (currentUser.isPresent() && currentUser.get().getPassword().equals(password)) {
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
        Optional<User> currentUser = userRepository.findByUsername(username);
        User loggedUser = getLoggedUser();
        if (loggedUser != null && loggedUser.getUsername().equals(username) && loggedUser.getPassword().equals(password)) {
            throw new InputDataConflictException("Вы уже выполнили вход");
        } else if (loggedUser != null) {
            throw new InputDataConflictException("Нельзя войти пока есть залогиненый пользователь: username(" + getLoggedUser().getUsername() + ")");
        }
        if (currentUser.isPresent() && currentUser.get().getPassword().equals(password)) {
            registeredUser = currentUser.get();
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