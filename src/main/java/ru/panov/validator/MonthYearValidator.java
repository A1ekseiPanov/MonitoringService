package ru.panov.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Валидатор для проверки корректности месяца и года.
 */
@Component
public class MonthYearValidator {
    public ValidatorResult isValid(int month, int year) {
        ValidatorResult validatorResult = new ValidatorResult();

        if (month < 1 || month > 12) {
            validatorResult.add(Error.of("В году 12 месяцев, c 1 по 12"));
        }

        if (year > LocalDate.now().getYear() || year < 1970) {
            validatorResult.add(Error.of("Можно выбрать показания с 1970 года по текущий год"));
        }
        return validatorResult;
    }
}