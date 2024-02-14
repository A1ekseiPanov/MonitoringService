package entity.dto;

import entity.MeterReading;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO для {@link MeterReading}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeterReadingDto implements Serializable {
    private Long typeId;
    private BigDecimal reading;
}