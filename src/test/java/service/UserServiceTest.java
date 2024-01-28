package service;

import exception.InputDataConflictException;
import exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.memory.MemoryUserRepository;

import static util.TestData.USER1;
import static util.TestData.USER2;

class UserServiceTest {
    private UserService userService;


    @BeforeEach
    void initEach() {
        UserService.resetInstance();
        MemoryUserRepository.resetInstance();
        this.userService = UserService.getInstance();
    }

    @Test
    void registerAndLoginTest() {
        userService.register(USER1.getUsername(), USER1.getPassword());
        userService.login(USER1.getUsername(), USER1.getPassword());

        Assertions.assertThat(userService.getLoggedUser().getUsername()).isEqualTo(USER1.getUsername());
        Assertions.assertThat(userService.getLoggedUser()).isNotNull();
    }

    @Test
    void registerFailTest() {
        userService.register(USER1.getUsername(), USER1.getPassword());
        Assertions.assertThatThrownBy(() -> {
                    userService.register(USER1.getUsername(), USER1.getPassword());
                }).isInstanceOf(InputDataConflictException.class)
                .hasMessage("Такой пользователь уже существует");
    }

    @Test
    void loginFailTest() {
        userService.register(USER1.getUsername(), USER1.getPassword());
        userService.register(USER2.getUsername(), USER2.getPassword());
        userService.login(USER1.getUsername(), USER1.getPassword());

        Assertions.assertThatThrownBy(() -> {
                    userService.login(USER1.getUsername(), USER1.getPassword());
                }).isInstanceOf(InputDataConflictException.class)
                .hasMessage("Вы уже выполнили вход");

        Assertions.assertThatThrownBy(() -> {
                    userService.login(USER2.getUsername(), USER2.getPassword());
                }).isInstanceOf(InputDataConflictException.class)
                .hasMessage("Нельзя войти пока есть залогиненый пользователь:" +
                        " username(" + userService.getLoggedUser().getUsername() + ")");
    }

    @Test
    void logoutTest() {
        userService.register(USER1.getUsername(), USER1.getPassword());
        userService.login(USER1.getUsername(), USER1.getPassword());

        Assertions.assertThat(userService.getLoggedUser().getUsername()).isEqualTo(USER1.getUsername());
        Assertions.assertThat(userService.getLoggedUser()).isNotNull();

        userService.logout();
        Assertions.assertThat(userService.getLoggedUser()).isNull();
    }

    @Test
    void logoutFailTest() {
        Assertions.assertThatThrownBy(() -> {
                    userService.logout();
                }).isInstanceOf(NotFoundException.class)
                .hasMessage("В данный момент ни один пользователь не вошел в систему.");
    }
}