package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс User представляет сущность пользователя.
 * Он содержит информацию об имени пользователя, пароле,
 * списке показаний счетчиков, а также роли пользователя.
 */
public class User {
    private Long id; // Уникальный идентификатор пользователя
    private String username; // Имя пользователя
    private String password; // Пароль пользователя
    private List<MeterReading> meterReadings; // Список показаний счетчиков, связанных с пользователем
    private String role; // Роль пользователя в системе

    public User( String username, String password) {
        this.username = username;
        this.password = password;
        this.meterReadings = new ArrayList<>();
        this.role = Role.USER.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<MeterReading> getMeterReadings() {
        return meterReadings;
    }

    public void setMeterReadings(List<MeterReading> meterReadings) {
        this.meterReadings = meterReadings;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(meterReadings, user.meterReadings) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, meterReadings, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", meterReadings=" + meterReadings +
                ", role='" + role + '\'' +
                '}';
    }
}