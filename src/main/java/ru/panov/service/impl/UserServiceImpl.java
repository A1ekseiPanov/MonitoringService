package ru.panov.service.impl;

import annotations.Audit;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.mapper.UserMapper;
import ru.panov.repository.UserRepository;
import ru.panov.service.UserService;

import java.util.Optional;

/**
 * Реализация сервиса для работы с пользователями.
 */
@Audit
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    /**
     * Регистрирует нового пользователя.
     *
     * @param dto данные нового пользователя
     * @throws InputDataConflictException если пользователь с таким именем уже существует
     */
    @Override
    public void register(UserRequestDTO dto) {
        Optional<User> currentUser = userRepository.findByUsername(dto.getUsername());
        if (currentUser.isPresent()) {
            throw new InputDataConflictException("Такой пользователь уже существует");
        } else {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            userRepository.save(mapper.userDtoToUser(dto));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User ‘" + username + "’ not found"));
    }
}