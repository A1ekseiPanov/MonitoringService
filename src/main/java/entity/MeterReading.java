package entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Класс MeterReading представляет сущность показаний счетчика.
 * Он содержит информацию о типе счетчика, показаниях, дате подачи показаний и связанном с ним пользователе.
 */
@Getter
@Setter
public class MeterReading {
    /**
     * Уникальный идентификатор.
     */
    private Long id;
    /**
     * Тип счетчика.
     */
    private TypeMeterReading type;
    /**
     * Показания счетчика.
     */
    private BigDecimal reading;
    /**
     * Дата подачи показаний счетчика.
     */
    private LocalDate localDate;
    /**
     * Пользователь, связанный с этими показаниями счетчика.
     */
    private User user;

    public MeterReading(TypeMeterReading type, BigDecimal reading) {
        this.type = type;
        this.reading = reading;
        this.localDate = LocalDate.now();
    }

    public MeterReading(TypeMeterReading type, BigDecimal reading,LocalDate localDate) {
        this.type = type;
        this.reading = reading;
        this.localDate = localDate;
    }

      @Override
    public String toString() {
        return "счетчик: " + type.getTitle() +
                ", показания: " + reading +
                ", дата подачи показаний: " + localDate ;
    }
}