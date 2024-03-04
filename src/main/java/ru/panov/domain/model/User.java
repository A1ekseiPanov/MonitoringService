package ru.panov.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
public class User implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}