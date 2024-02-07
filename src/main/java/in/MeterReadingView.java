package in;

import controller.MeterReadingController;
import controller.UserController;
import entity.MeterReading;
import entity.Role;
import entity.TypeMeterReading;
import entity.User;
import service.TypeMeterReadingService;
import util.AuditLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс MeterReadingView представляет пользовательский интерфейс для взаимодействия с данными о показаниях счетчиков.
 * Он обрабатывает действия пользователей, связанные с передачей показаний, просмотром истории показаний и другими функциями.
 */
public class MeterReadingView {
    private static final MeterReadingView INSTANCE = new MeterReadingView();
    private final TypeMeterReadingService typeMeterReadingService;
    private final Scanner scanner = new Scanner(System.in);
    private final UserController userController;
    private final MeterReadingController meterReadingController;

    private MeterReadingView() {
        this.typeMeterReadingService = TypeMeterReadingService.getInstance();
        this.userController = UserController.getInstance();
        this.meterReadingController = MeterReadingController.getInstance();
    }

    public static MeterReadingView getInstance() {
        return INSTANCE;
    }

    /**
     * Выполняет действия для пользователя, который вошел в систему.
     * Выводит меню и обрабатывает выбор пользователя в зависимости от его роли (пользователь или администратор).
     */
    public void performLoggedInActions() {
        User loggedUser = userController.loggedUser();
        if (loggedUser == null) {
            System.out.println("Сначала вам нужно войти в систему.");
            return;
        }
        if (Objects.equals(loggedUser.getRole(), Role.USER.toString())) {
            menuActionsUser();
        } else {
            menuActionsAdmin();
        }
    }

    private void menuActionsAdmin() {
        while (true) {
            printLoggedAdminInMenu();

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    printAllMeterReadingsByMonth();
                    break;
                case 2:
                    printReadingHistory();
                    break;
                case 3:
                    AuditLog.getLog().forEach(System.out::println);
                    break;
                case 4:
                    addingType();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void menuActionsUser() {
        while (true) {
            printLoggedInMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    submitMeterReading();
                    break;
                case 2:
                    printLastMeterReading();
                    break;
                case 3:
                    printAllMeterReadingsByMonth();
                    break;
                case 4:
                    printReadingHistory();
                    break;

                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printLoggedInMenu() {
        System.out.println("Меню передачи показаний:");
        System.out.println("1. Передать показания счетчика");
        System.out.println("2. Актуальные показания счетчиков(последние поданые показания)");
        System.out.println("3. Показаний за конкретный месяц");
        System.out.println("4. История подачи показаний");
        System.out.println("5. Вернуться в главное меню");
    }

    private void printLoggedAdminInMenu() {
        System.out.println("Меню показаний:");
        System.out.println("1. Показания пользователей за конкретный месяц");
        System.out.println("2. Показания пользователей");
        System.out.println("3. Аудит действий пользователей");
        System.out.println("4. Добавление счетчиков");
        System.out.println("5. Вернуться в главное меню");
    }

    private int getUserChoice() {
        System.out.print("Выберете пункт из меню: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Печатает последние(актуальные) показания счетчиков пользователя.
     */
    private void printLastMeterReading() {
        List<MeterReading> latestReadings = meterReadingController.getLatestReadingsByTypes();
        printListMeterReading(latestReadings);
    }

    /**
     * Печатает историю подачи показаний счетчиков пользователя.
     */
    private void printReadingHistory() {
        List<MeterReading> userReadings = meterReadingController.getAllReadings();
        printListMeterReading(userReadings);
    }

    /**
     * Печатает список показаний счетчиков.
     *
     * @param meterReadings Список показаний счетчиков для печати
     */
    private void printListMeterReading(List<MeterReading> meterReadings) {
        meterReadings.forEach(reading -> {
            System.out.println("Пользователь: " + reading.getUser().getUsername());
            System.out.println("Дата: " + reading.getLocalDate());
            System.out.println("Счетчик: " + reading.getType().getTitle());
            System.out.println("Показания: " + reading.getReading());
            System.out.println("-------------");
        });
    }

    /**
     * Печатает список типов счетчиков.
     */
    private List<TypeMeterReading> printTypeMeterReading() {
        List<TypeMeterReading> typeMeterReadingList = typeMeterReadingService.getAll();
        typeMeterReadingList.forEach(t -> System.out.println(t.getId() + ". " + t.getTitle()));
        return typeMeterReadingList;
    }

    /**
     * Пользователь передает показания для определенного типа счетчика.
     */
    private void submitMeterReading() {
        System.out.println("Счетчик:");
        List<TypeMeterReading> typeMeterReadingList = printTypeMeterReading();
        TypeMeterReading meterReading;
        do {
            int choice = getUserChoice();
            meterReading = typeMeterReadingList.stream()
                    .filter(typeMeterReading -> typeMeterReading.getId() == choice)
                    .findAny()
                    .orElse(null);
            if(meterReading==null) {
                System.out.println("Такого счетчика нет");
            }
        } while (meterReading == null);

        System.out.println("Показания:");
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        BigDecimal reading = scanner.nextBigDecimal();
        while (reading.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Показания счетчиков должны быть больше 0");
            reading = scanner.nextBigDecimal();

        }
        if (meterReading != null) {
            meterReadingController.submitMeterReading(meterReading, reading);
        }
    }

    /**
     * Запрашивает у пользователя месяц и год и возвращает список показаний счетчиков за указанный месяц и год.
     *
     * @return Список показаний счетчиков за указанный месяц и год
     */
    private List<MeterReading> submitMonthYear() {
        System.out.println("Введите месяц от 1 до 12");
        while (!scanner.hasNextInt()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        int month = scanner.nextInt();
        while (month < 1 || month > 12) {
            System.out.println("В году 12 месяцев, c 1 по 12");
            month = scanner.nextInt();
        }
        System.out.println("Введите год");
        while (!scanner.hasNextInt()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        int year = scanner.nextInt();
        while (year > LocalDate.now().getYear() || year < 1970) {
            System.out.println("Можно выбрать показания с 1970 года по текущий год");
            year = scanner.nextInt();
        }
        return meterReadingController.getAllMeterReadingsByMonth(month, year);
    }

    private void addingType() {
        System.out.println("Введите название типа счетчика:");
        scanner.nextLine();
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        String title = scanner.nextLine();
        try {
            typeMeterReadingService.addingType(title);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAllMeterReadingsByMonth() {
        printListMeterReading(submitMonthYear());
    }
}