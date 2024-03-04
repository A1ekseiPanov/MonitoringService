package service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.panov.exception.InputDataConflictException;
import ru.panov.mapper.UserMapper;
import ru.panov.repository.UserRepository;
import ru.panov.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static util.TestData.USER1;
import static util.TestData.USER_REQUEST_DTO;

class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper mapper;
    @Mock
    private PasswordEncoder encoder;

    @BeforeEach
    void initEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Регистрация пользователя")
    void registerTest() {
        when(userRepository.findByUsername(USER_REQUEST_DTO.getUsername())).thenReturn(Optional.empty());
        when(mapper.userDtoToUser(USER_REQUEST_DTO)).thenReturn(USER1);

        assertThatCode(() -> userService.register(USER_REQUEST_DTO))
                .doesNotThrowAnyException();
        verify(userRepository, times(1)).save(USER1);
    }

    @Test
    @DisplayName("Регистрация пользователя (пользователь уже существует)")
    void registerUserIsPresentTest() {
        when(userRepository.findByUsername(USER_REQUEST_DTO.getUsername()))
                .thenReturn(Optional.ofNullable(USER1));

        assertThatThrownBy(() ->
                userService.register(USER_REQUEST_DTO))
                .isInstanceOf(InputDataConflictException.class)
                .hasMessage("Такой пользователь уже существует");
    }
}