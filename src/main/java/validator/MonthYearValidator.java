package validator;

import java.time.LocalDate;

public class MonthYearValidator {
    private static final MonthYearValidator INSTANCE = new MonthYearValidator();

    private MonthYearValidator() {
    }

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

    public static MonthYearValidator getInstance() {
        return INSTANCE;
    }
}
