package in;

import controller.UserController;

import java.util.Scanner;

/**
 * Класс UserView представляет представление пользовательского интерфейса для взаимодействия с пользователями.
 * Он содержит методы для регистрации, входа и выхода пользователя.
 */
public class UserView {
    private static final Scanner scanner = new Scanner(System.in);
    private final UserController userController = UserController.getInstance();
    private static final UserView INSTANCE = new UserView();

    public UserView() {
    }

    public static UserView getInstance() {
        return INSTANCE;
    }

    /**
     * Регистрирует нового пользователя, запрашивая у пользователя имя пользователя и пароль.
     */
    public void registerUser() {
        System.out.println("Регистрация:");
        System.out.print("Введите username: ");
        String username = scanner.next();
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        userController.register(username, password);
    }

    /**
     * Входит в систему, запрашивая у пользователя имя пользователя и пароль.
     */
    public void login() {
        System.out.println("Вход:");
        System.out.print("Введите username: ");
        String username = scanner.next();
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        userController.login(username, password);
    }

    /**
     * Выходит из системы.
     */
    public void logout() {
        userController.logout();
    }
}