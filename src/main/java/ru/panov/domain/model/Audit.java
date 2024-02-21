package ru.panov.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * Представляет запись аудита в системе.
 */
@Getter
@Setter
@AllArgsConstructor
public class Audit {
    /**
     * Уникальный идентификатор записи аудита.
     */
    private Long id;
    /**
     * Дата и время создания записи аудита.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;
    /**
     * Сообщение, связанное с записью аудита.
     */
    private String message;

    public Audit(String message) {
        this.localDateTime = LocalDateTime.now();
        this.message = message;
    }
}