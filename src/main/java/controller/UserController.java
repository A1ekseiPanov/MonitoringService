package controller;

import entity.User;
import exception.InputDataConflictException;
import exception.NotFoundException;
import service.UserService;


/**
 * Класс UserController отвечает за управление пользователями в системе.
 * Он предоставляет методы для регистрации, входа и выхода пользователя.
 */
public class UserController {
    private static final UserController INSTANCE = new UserController();
    private final UserService userService = UserService.getInstance();

    private UserController() {
    }

    public static UserController getInstance() {
        return INSTANCE;
    }

    public void register(String username, String password) {
        try {
            userService.register(username, password);
        } catch (InputDataConflictException e) {
            System.out.println(e.getMessage());
        }
    }

    public void login(String username, String password) {
        try {
            userService.login(username, password);
        } catch (InputDataConflictException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void logout() {
        try {
            userService.logout();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public User loggedUser() {
        return userService.getLoggedUser();
    }
}