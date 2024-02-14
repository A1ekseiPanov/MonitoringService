package service;

import annotations.Audit;
import exception.InputDataConflictException;
import exception.ValidationException;
import mapper.UserMapper;
import repository.UserRepository;
import entity.dto.UserDto;
import entity.User;
import exception.NotFoundException;
import validator.UserRegisterValidator;
import validator.ValidatorResult;
import util.AuditLog;

import java.util.Optional;

/**
 * Класс UserService предоставляет методы для работы с пользователями в системе.
 * Он обеспечивает функции регистрации, входа и выхода пользователя.
 */
@Audit
public class UserService {
    private User registeredUser;
    private final UserRepository userRepository;
    private final UserRegisterValidator userRegisterValidator;
    private final UserMapper mapper = UserMapper.INSTANCE;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRegisterValidator = UserRegisterValidator.getInstance();
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
        UserDto userDto = new UserDto(username, password);
        ValidatorResult validatorResult = userRegisterValidator.isValid(userDto);
        if (!validatorResult.isValid()) {
            throw new ValidationException(validatorResult.getErrors());
        }
        if (currentUser.isPresent() || currentUser.get().getUsername().equals(username)) {
            throw new InputDataConflictException("Такой пользователь уже существует");
        } else {
            userRepository.save(mapper.userDtoToUser(userDto));
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
    public User login(String username, String password) {
        Optional<User> currentUser = userRepository.findByUsername(username);
        User loggedUser = getLoggedUser();
        if (loggedUser != null && loggedUser.getUsername().equals(username) && loggedUser.getPassword().equals(password)) {
            throw new InputDataConflictException("Вы уже выполнили вход");
        }
        if (currentUser.isPresent() && currentUser.get().getPassword().equals(password)) {
            AuditLog.logAction("Пользователь username(" + username + ") вошел в систему");
            return registeredUser = currentUser.get();
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

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с id:%s не найден", id)));
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