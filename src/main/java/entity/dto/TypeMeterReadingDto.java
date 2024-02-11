package entity.dto;

import entity.TypeMeterReading;
import lombok.*;

import java.io.Serializable;

/**
 * DTO для {@link TypeMeterReading}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeMeterReadingDto implements Serializable {
    private String title;
}