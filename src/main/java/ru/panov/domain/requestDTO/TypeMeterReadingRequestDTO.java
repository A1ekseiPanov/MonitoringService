package ru.panov.domain.requestDTO;

import ru.panov.domain.model.TypeMeterReading;
import lombok.*;

import java.io.Serializable;

/**
 * DTO (используются при запросе) для {@link TypeMeterReading}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeMeterReadingRequestDTO implements Serializable {
    private String title;
}