package validator;

import entity.dto.MeterReadingDto;

import java.math.BigDecimal;

public class MeterReadingValidator {
    private static final MeterReadingValidator INSTANCE = new MeterReadingValidator();

    private MeterReadingValidator() {
    }

    public ValidatorResult isValid(MeterReadingDto dto) {
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

    public static MeterReadingValidator getInstance() {
        return INSTANCE;
    }
}

