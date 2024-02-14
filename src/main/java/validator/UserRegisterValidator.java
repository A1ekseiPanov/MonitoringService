package validator;

import entity.dto.UserDto;


public final class UserRegisterValidator implements Validator<UserDto> {
    private static final int MIN_USERNAME_LENGTH = 2;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 2;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]+$";
    private static final UserRegisterValidator INSTANCE = new UserRegisterValidator();

    private UserRegisterValidator() {
    }

    public ValidatorResult isValid(UserDto userDto) {
        ValidatorResult validatorResult = new ValidatorResult();

        if (userDto.getUsername() == null) {
            validatorResult.add(Error.of("Username не должено быть null"));
        } else if (userDto.getUsername().length() <= MIN_USERNAME_LENGTH) {
            validatorResult.add(Error.of("Минимальная длина username должна быть больша "
                    + MIN_USERNAME_LENGTH));
        } else if (userDto.getUsername().length() >= MAX_USERNAME_LENGTH) {
            validatorResult.add(Error.of("Максимальная длина username должна быть меньше "
                    + MAX_USERNAME_LENGTH));
        } else if (!userDto.getUsername().matches(USERNAME_REGEX)) {
            validatorResult.add(Error.of("Username должен остоять только " +
                    "из комбинации латинских букв (в любом регистре)," +
                    " цифр и символа подчеркивания"));
        }

        if (userDto.getPassword() == null) {
            validatorResult.add(Error.of("Пароль не должен быть null"));
        } else if (userDto.getPassword().length() <= MIN_PASSWORD_LENGTH) {
            validatorResult.add(Error.of("Минимальная длина пароля должна быть больша " + MIN_PASSWORD_LENGTH));
        } else if (userDto.getUsername().length() >= MAX_PASSWORD_LENGTH) {
            validatorResult.add(Error.of("Максимальная длина пароля должна быть меньше " + MAX_PASSWORD_LENGTH));
        }
        return validatorResult;
    }

    public static UserRegisterValidator getInstance() {
        return INSTANCE;
    }
}