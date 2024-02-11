package entity.dto;

import entity.User;
import lombok.*;

import java.io.Serializable;

/**
 * DTO для {@link User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {
    private String username;
    private String password;
}