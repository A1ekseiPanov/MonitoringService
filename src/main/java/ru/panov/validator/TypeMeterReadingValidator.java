package ru.panov.validator;

import org.springframework.stereotype.Component;
import ru.panov.domain.requestDTO.TypeMeterReadingRequestDTO;

/**
 * Валидатор для проверки корректности данных о типе счетчика.
 */
@Component
public class TypeMeterReadingValidator implements Validator<TypeMeterReadingRequestDTO> {
    private static final int MIN_TITLE_LENGTH = 2;
    private static final int MAX_TITLE_LENGTH = 20;

    private static final String TITLE_REGEX = "^[А-ЯЁA-Z][а-яёa-z]*$";

    public ValidatorResult isValid(TypeMeterReadingRequestDTO dto) {
        ValidatorResult validatorResult = new ValidatorResult();

        if (dto.getTitle() == null) {
            validatorResult.add(Error.of("Title не должен быть null"));
        } else if (dto.getTitle().length() <= MIN_TITLE_LENGTH) {
            validatorResult.add(Error.of("Минимальная длина title должна быть больша "
                                         + MIN_TITLE_LENGTH));
        } else if (dto.getTitle().length() >= MAX_TITLE_LENGTH) {
            validatorResult.add(Error.of("Максимальная длина title должна быть меньше "
                                         + MAX_TITLE_LENGTH));
        } else if (!dto.getTitle().matches(TITLE_REGEX)) {
            validatorResult.add(Error.of("Title начинается с заглавной буквы и " +
                                         "содержит только строчные буквы после неё."));
        }
        return validatorResult;
    }
}