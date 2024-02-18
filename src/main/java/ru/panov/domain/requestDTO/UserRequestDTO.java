package ru.panov.domain.requestDTO;

import ru.panov.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO (используются при запросе) для {@link User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDTO implements Serializable {
    private String username;
    private String password;
}