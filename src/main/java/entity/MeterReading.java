package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Класс MeterReading представляет сущность показаний счетчика.
 * Он содержит информацию о типе счетчика, показаниях, дате подачи показаний и связанном с ним пользователе.
 */
public class MeterReading {
    private Long id; // Уникальный идентификатор
    private TypeMeterReading type; // Тип счетчика.
    private BigDecimal reading; // Показания счетчика.
    private LocalDate localDate; // Дата подачи показаний счетчика.
    private User user; // Пользователь, связанный с этими показаниями счетчика.


    public MeterReading(TypeMeterReading type, BigDecimal reading) {
        this.type = type;
        this.reading = reading;
        this.localDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMeterReading getType() {
        return type;
    }

    public void setType(TypeMeterReading type) {
        this.type = type;
    }

    public BigDecimal getReading() {
        return reading;
    }

    public void setReading(BigDecimal reading) {
        this.reading = reading;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "счетчик: " + type.getTitle() +
                ", показания: " + reading +
                ", дата подачи показаний: " + localDate ;
    }
}