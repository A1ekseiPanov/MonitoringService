package util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс AuditLog предоставляет статические методы для регистрации действий и доступа к журналу аудита.
 * Он позволяет регистрировать действия в системе и получать доступ к журналу аудита для анализа и отслеживания.
 */
public class AuditLog {
    private static List<String> log = new ArrayList<>();

    public static void logAction(String action) {
        log.add(LocalDateTimeFormatter.formatter(LocalDateTime.now()) + " " + action);
        System.out.println(action);
    }

    public static List<String> getLog() {
        return log;
    }
}