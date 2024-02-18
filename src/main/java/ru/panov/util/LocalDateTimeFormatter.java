package ru.panov.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatter(LocalDateTime localDateTime) {
        return localDateTime.format(CUSTOM_FORMATTER);
    }
}