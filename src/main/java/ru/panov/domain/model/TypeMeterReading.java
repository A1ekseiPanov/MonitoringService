package ru.panov.domain.model;

import lombok.*;

/**
 * Класс, представляющий тип счетчика для показаний.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class TypeMeterReading {
    /**
     * Уникальный идентификатор типа счетчика.
     */
    private Long id;
    /**
     * Наименование типа счетчика.
     */
    private String title;

    public TypeMeterReading(String title) {
        this.title = title;
    }
}