package ru.panov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.panov.annotations.Audit;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.domain.responseDTO.UserResponseDTO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.mapper.UserMapper;
import ru.panov.repository.UserRepository;
import ru.panov.service.UserService;
import ru.panov.validator.Validator;
import ru.panov.validator.ValidatorResult;

import java.util.Optional;

/**
 * Реализация сервиса для работы с пользователями.
 */
@Audit
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final Validator<UserRequestDTO> userRegisterValidator;

    /**
     * Регистрирует нового пользователя.
     *
     * @param dto данные нового пользователя
     * @throws ValidationException        если данные не прошли валидацию
     * @throws InputDataConflictException если пользователь с таким именем уже существует
     */
    @Override
    public void register(UserRequestDTO dto) {
        ValidatorResult validatorResult = userRegisterValidator.isValid(dto);
        if (!validatorResult.isValid()) {
            throw new ValidationException(validatorResult.getErrors().toString());
        }
        Optional<User> currentUser = userRepository.findByUsername(dto.getUsername());
        if (currentUser.isPresent()) {
            throw new InputDataConflictException("Такой пользователь уже существует");
        } else {
            userRepository.save(mapper.userDtoToUser(dto));
        }
    }

    /**
     * Вход пользователя в систему.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return данные пользователя
     * @throws IllegalArgumentException если имя пользователя или пароль неверны
     */
    @Override
    public UserResponseDTO login(String username, String password) {
        Optional<User> currentUser = userRepository.findByUsername(username);
        if (currentUser.isPresent() && currentUser.get().getPassword().equals(password)) {
            return mapper.userToUserResponseDto(currentUser.get());
        } else {
            throw new IllegalArgumentException("Неверное имя пользователя или пароль. Ошибка входа.");
        }
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с id:%s не найден", id)));
    }
}