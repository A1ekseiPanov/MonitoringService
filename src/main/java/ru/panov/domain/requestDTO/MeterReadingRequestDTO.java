package ru.panov.domain.requestDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Выберете счетчик по его id(обязательно)")
    @Min(value = 1, message = "id счетчика начинается с 1")
    private Long typeId;

    @NotNull(message = "Показния должны быть заполнены")
    @DecimalMin(value = "1", message = "Показания счетчиков должны быть больше 0")
    private BigDecimal reading;
}