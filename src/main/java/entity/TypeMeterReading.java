package entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий тип счетчика для показаний.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
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