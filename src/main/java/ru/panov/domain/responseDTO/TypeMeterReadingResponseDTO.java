package ru.panov.domain.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.panov.domain.model.TypeMeterReading;

import java.io.Serializable;

/**
 * DTO (возвращается в качестве ответа) для {@link TypeMeterReading}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeMeterReadingResponseDTO implements Serializable {
    private Long id;
    private String title;
}