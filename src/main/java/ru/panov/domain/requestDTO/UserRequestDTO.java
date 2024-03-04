package ru.panov.domain.requestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.panov.domain.model.User;

import java.io.Serializable;

/**
 * DTO (используются при запросе) для {@link User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDTO implements Serializable {
    @Size(min = 2, message = "Минимальная длина username должна быть больше 2")
    @NotNull(message = "Username не должено быть null")
    @Pattern(regexp = "^[\\p{L}\\p{N}_]+$", message = "Username должен остоять только " +
            "из комбинации латинских букв (в любом регистре)," +
            " цифр и символа подчеркивания")
    private String username;

    @NotNull(message = "Пароль не должен быть null")
    @Size(min = 2, message = "Минимальная длина пароля должна быть больше 2")
    private String password;
}