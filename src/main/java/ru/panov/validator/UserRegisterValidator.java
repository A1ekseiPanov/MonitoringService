package ru.panov.validator;

import org.springframework.stereotype.Component;
import ru.panov.domain.requestDTO.UserRequestDTO;

/**
 * Валидатор для проверки корректности данных пользователя при регистрации.
 */
@Component
public final class UserRegisterValidator implements Validator<UserRequestDTO> {
    private static final int MIN_USERNAME_LENGTH = 2;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 2;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final String USERNAME_REGEX = "^[\\p{L}\\p{N}_]+$";

    @Override
    public ValidatorResult isValid(UserRequestDTO userRequestDTO) {
        ValidatorResult validatorResult = new ValidatorResult();

        if (userRequestDTO.getUsername() == null) {
            validatorResult.add(Error.of("Username не должено быть null"));
        } else if (userRequestDTO.getUsername().length() <= MIN_USERNAME_LENGTH) {
            validatorResult.add(Error.of("Минимальная длина username должна быть больша "
                                         + MIN_USERNAME_LENGTH));
        } else if (userRequestDTO.getUsername().length() >= MAX_USERNAME_LENGTH) {
            validatorResult.add(Error.of("Максимальная длина username должна быть меньше "
                                         + MAX_USERNAME_LENGTH));
        } else if (!userRequestDTO.getUsername().matches(USERNAME_REGEX)) {
            validatorResult.add(Error.of("Username должен остоять только " +
                                         "из комбинации латинских букв (в любом регистре)," +
                                         " цифр и символа подчеркивания"));
        }

        if (userRequestDTO.getPassword() == null) {
            validatorResult.add(Error.of("Пароль не должен быть null"));
        } else if (userRequestDTO.getPassword().length() <= MIN_PASSWORD_LENGTH) {
            validatorResult.add(Error.of("Минимальная длина пароля должна быть больша " + MIN_PASSWORD_LENGTH));
        } else if (userRequestDTO.getUsername().length() >= MAX_PASSWORD_LENGTH) {
            validatorResult.add(Error.of("Максимальная длина пароля должна быть меньше " + MAX_PASSWORD_LENGTH));
        }
        return validatorResult;
    }
}