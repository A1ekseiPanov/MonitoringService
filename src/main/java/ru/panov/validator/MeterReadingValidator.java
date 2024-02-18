package ru.panov.validator;

import org.springframework.stereotype.Component;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;

import java.math.BigDecimal;

/**
 * Валидатор для проверки данных показаний счетчика.
 */
@Component
public class MeterReadingValidator implements Validator<MeterReadingRequestDTO> {
    public ValidatorResult isValid(MeterReadingRequestDTO dto) {
        ValidatorResult validatorResult = new ValidatorResult();

        if (dto.getTypeId() == null) {
            validatorResult.add(Error.of("Выберете счетчик по его id(обязательно)"));
        } else if (dto.getTypeId() <= 0) {
            validatorResult.add(Error.of("id счетчика начинается с 1"));
        }

        if (dto.getReading() == null) {
            validatorResult.add(Error.of("Показния должны быть заполнены"));
        } else if (dto.getReading().compareTo(BigDecimal.ZERO) <= 0) {
            validatorResult.add(Error.of("Показания счетчиков должны быть больше 0"));
        }

        return validatorResult;
    }
}