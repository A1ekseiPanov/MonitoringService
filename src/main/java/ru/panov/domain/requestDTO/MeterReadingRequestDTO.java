package ru.panov.domain.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.panov.domain.model.MeterReading;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO(используются при запросе) для {@link MeterReading}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeterReadingRequestDTO implements Serializable {
    private Long typeId;
    private BigDecimal reading;
}