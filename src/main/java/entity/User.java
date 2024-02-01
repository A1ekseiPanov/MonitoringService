package entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс User представляет сущность пользователя.
 * Он содержит информацию об имени пользователя, пароле,
 * списке показаний счетчиков, а также роли пользователя.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;
    /**
     * Имя пользователя.
     */
    private String username;
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Список показаний счетчиков, связанных с пользователем.
     */
    private List<MeterReading> meterReadings;
    /**
     * Роль пользователя в системе.
     */
    private String role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.meterReadings = new ArrayList<>();
        this.role = Role.USER.toString();
    }
}