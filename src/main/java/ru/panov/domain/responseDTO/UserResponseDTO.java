package ru.panov.domain.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.panov.domain.model.User;

import java.io.Serializable;

/**
 * DTO (возвращается в качестве ответа) для {@link User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO implements Serializable {
    private Long id;
    private String username;
}