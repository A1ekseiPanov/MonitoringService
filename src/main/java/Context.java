import controller.MeterReadingController;
import controller.UserController;
import in.MeterReadingView;
import in.UserView;
import repository.MeterReadingRepository;
import repository.TypeMeterReadingRepository;
import repository.jdbc.JdbcMeterReadingRepository;
import repository.jdbc.JdbcTypeMeterReadingRepository;
import repository.jdbc.JdbcUserRepository;
import service.MeterReadingService;
import service.TypeMeterReadingService;
import service.UserService;

import java.util.Scanner;

/**
 * Класс Context представляет основной контекст приложения.
 * Он содержит методы для запуска и управления приложением.
 */
public class Context {
    private final Scanner scanner;
    private final JdbcUserRepository userRepository;
    private final UserService userService;
    private final UserController userController;
    private final UserView userView;
    private final TypeMeterReadingRepository typeMeterReadingRepository;
    private final TypeMeterReadingService typeMeterReadingService;
    private final MeterReadingRepository meterReadingRepository;
    private final MeterReadingService meterReadingService;
    private final MeterReadingController meterReadingController;
    private final MeterReadingView meterReadingView;

    public Context() {
        this.scanner = new Scanner(System.in);
        this.userRepository = new JdbcUserRepository();
        this.userService = new UserService(userRepository);
        this.userController = new UserController(userService);
        this.userView = new UserView(scanner,userController);
        this.typeMeterReadingRepository = new JdbcTypeMeterReadingRepository();
        this.typeMeterReadingService = new TypeMeterReadingService(typeMeterReadingRepository);
        this.meterReadingRepository = new JdbcMeterReadingRepository(userRepository,typeMeterReadingRepository);
        this.meterReadingService = new MeterReadingService(userService,meterReadingRepository,typeMeterReadingService);
        this.meterReadingController = new MeterReadingController(meterReadingService);
        this.meterReadingView = new MeterReadingView(typeMeterReadingService,scanner,userController,meterReadingController);
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