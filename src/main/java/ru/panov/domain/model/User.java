package ru.panov.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @JsonProperty("userId")
    private Long id;
    /**
     * Имя пользователя.
     */
    private String username;
    /**
     * Пароль пользователя.
     */
    @JsonIgnore
    private String password;
    /**
     * Список показаний счетчиков, связанных с пользователем.
     */
    @JsonIgnore
    private List<MeterReading> meterReadings;
    /**
     * Роль пользователя в системе.
     */
    private String role;

    public User() {
        this.meterReadings = new ArrayList<>();
        this.role = Role.USER.toString();
    }

    public User(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.meterReadings = new ArrayList<>();
        this.role = role;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.meterReadings = new ArrayList<>();
        this.role = Role.USER.toString();
    }
}