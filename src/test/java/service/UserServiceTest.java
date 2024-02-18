package service;


import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.mapper.UserMapper;
import ru.panov.repository.UserRepository;
import ru.panov.service.impl.UserServiceImpl;
import ru.panov.validator.Validator;
import ru.panov.validator.ValidatorResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static util.TestData.USER1;

class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private  UserMapper mapper;
    @Mock
    private  HttpSession session;
    @Mock
    private Validator<UserRequestDTO> userRegisterValidator;
    @Mock
    private ValidatorResult validatorResult;

    @BeforeEach
    void initEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Регистрация пользователя")
    void registerTest() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(USER1.getUsername(),USER1.getPassword());

        when(userRegisterValidator.isValid(userRequestDTO)).thenReturn(new ValidatorResult());
        when(validatorResult.isValid()).thenReturn(true);
        when(userRepository.findByUsername(userRequestDTO.getUsername())).thenReturn(Optional.empty());
        when(mapper.userDtoToUser(userRequestDTO)).thenReturn(USER1);

        assertThatCode(() -> userService.register(userRequestDTO))
                .doesNotThrowAnyException();
        verify(userRepository, times(1)).save(USER1);
    }

    @Test
    @DisplayName("Регистрация пользователя (пользователь уже существует)")
    void registerUserIsPresentTest() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(USER1.getUsername(),USER1.getPassword());

        when(userRegisterValidator.isValid(userRequestDTO)).thenReturn(new ValidatorResult());
        when(validatorResult.isValid()).thenReturn(true);
        when(userRepository.findByUsername(USER1.getUsername()))
                .thenReturn(Optional.ofNullable(USER1));

        assertThatThrownBy(() ->
            userService.register(userRequestDTO))
                .isInstanceOf(InputDataConflictException.class)
                .hasMessage("Такой пользователь уже существует");
    }

    @Test
    @DisplayName("Вход пользователя")
    void loginAndLogoutTest() {
        when(userRepository.findByUsername(USER1.getUsername()))
                .thenReturn(Optional.ofNullable(USER1));

        userService.login(USER1.getUsername(), USER1.getPassword());
        when(session.getAttribute("user")).thenReturn(USER1);

        assertThat(session.getAttribute("user")).isEqualTo(USER1);
        assertThat(session.getAttribute("user")).isNotNull();

    }


}