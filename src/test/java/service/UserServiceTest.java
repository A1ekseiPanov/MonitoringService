package service;

import entity.User;
import exception.InputDataConflictException;
import exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static util.TestData.USER1;
import static util.TestData.USER2;

class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void initEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Регистрация пользователя")
    void registerTest() {
        when(userRepository.findByUsername(USER1.getUsername())).thenReturn(Optional.empty());

        userService.register(USER1.getUsername(),USER1.getPassword());

        verify(userRepository).findByUsername(USER1.getUsername());
        verify(userRepository).save(any(User.class));
   }

    @Test
    @DisplayName("Регистрация пользователя (пользователь уже существует)")
    void registerUserIsPresentTest() {
        when(userRepository.findByUsername(USER1.getUsername()))
                .thenReturn(Optional.ofNullable(USER1));
        assertThatThrownBy(() -> {
                    userService.register(USER1.getUsername(), USER1.getPassword());
                }).isInstanceOf(InputDataConflictException.class)
                .hasMessage("Такой пользователь уже существует");
    }

    @Test
    @DisplayName("Вход пользователя (ошибка входа)")
    void loginFailTest() {
        when(userRepository.findByUsername(USER1.getUsername()))
                .thenReturn(Optional.ofNullable(USER1));

        userService.login(USER1.getUsername(), USER1.getPassword());

        assertThatThrownBy(() -> {
                    userService.login(USER1.getUsername(), USER1.getPassword());
                }).isInstanceOf(InputDataConflictException.class)
                .hasMessage("Вы уже выполнили вход");

        assertThatThrownBy(() -> {
                    userService.login(USER2.getUsername(), USER2.getPassword());
                }).isInstanceOf(InputDataConflictException.class)
                .hasMessage("Нельзя войти пока есть залогиненый пользователь:" +
                        " username(" + userService.getLoggedUser().getUsername() + ")");
    }

    @Test
    @DisplayName("Вход и выход пользователя")
    void loginAndLogoutTest() {
        when(userRepository.findByUsername(USER1.getUsername()))
                .thenReturn(Optional.ofNullable(USER1));

        userService.login(USER1.getUsername(), USER1.getPassword());

        assertThat(userService.getLoggedUser().getUsername()).isEqualTo(USER1.getUsername());
        assertThat(userService.getLoggedUser()).isNotNull();

        userService.logout();
        assertThat(userService.getLoggedUser()).isNull();

    }

    @Test
    @DisplayName("Выход пользователя (пользователь не вошел в систему)")
    void logoutUserNotLogInTest() {
        assertThatThrownBy(() -> {
                    userService.logout();
                }).isInstanceOf(NotFoundException.class)
                .hasMessage("В данный момент ни один пользователь не вошел в систему.");
    }
}