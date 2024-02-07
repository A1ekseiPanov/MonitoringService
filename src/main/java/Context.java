import in.MeterReadingView;
import in.UserView;

import java.util.Scanner;

/**
 * Класс Context представляет основной контекст приложения.
 * Он содержит методы для запуска и управления приложением.
 */
public class Context {
    private final Scanner scanner;
    private final UserView userView;
    private final MeterReadingView meterReadingView;

    public Context() {
        this.scanner = new Scanner(System.in);
        this.userView = UserView.getInstance();
        this.meterReadingView = MeterReadingView.getInstance();
    }

    /**
     * Запускает приложение и обрабатывает основной цикл.
     */
    public void run() {
        while (true) {
            printMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> userView.registerUser();
                case 2 -> userView.login();
                case 3 -> meterReadingView.performLoggedInActions();
                case 4 -> userView.logout();
                case 5 -> {
                    System.out.println("Выход из приложения. До свидания!");
                    System.exit(0);
                }
                default -> System.out.println("Такого пункта меню не существует. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private int getUserChoice() {
        System.out.print("Выберете пункт из меню: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void printMainMenu() {
        System.out.println("Monitoring Service Menu:");
        System.out.println("1. Регистрация");
        System.out.println("2. Логин");
        System.out.println("3. Действия после входа в систему");
        System.out.println("4. Выйти из системы");
        System.out.println("5. Выход");
    }
}