package ru.panov.domain.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MeterReading {
    /**
     * Уникальный идентификатор.
     */
    private Long id;
    /**
     * Идентификатор типа счетчика.
     */
    private Long typeId;
    /**
     * Показания счетчика.
     */
    private BigDecimal reading;
    /**
     * Дата подачи показаний счетчика.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate localDate;
    /**
     * Идентификатор пользователя, связанного с этими показаниями счетчика.
     */
    private Long userId;

    public MeterReading() {
        this.localDate = LocalDate.now();
    }

    public MeterReading(long typeId, BigDecimal reading) {
        this.typeId = typeId;
        this.reading = reading;
        this.localDate = LocalDate.now();
    }

    public MeterReading(long typeId, BigDecimal reading, LocalDate localDate) {
        this.typeId = typeId;
        this.reading = reading;
        this.localDate = localDate;
    }

    @Override
    public String toString() {
        return "счетчик: " + typeId +
                ", показания: " + reading +
                ", дата подачи показаний: " + localDate;
    }
}