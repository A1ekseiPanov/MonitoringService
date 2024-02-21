package ru.panov.domain.responseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.panov.domain.model.MeterReading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO (возвращается в качестве ответа) для {@link MeterReading}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeterReadingResponseDTO implements Serializable {
    private Long userId;
    private String typeMR;
    private BigDecimal reading;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate localDate;
}