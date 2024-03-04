package ru.panov.domain.requestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.panov.domain.model.TypeMeterReading;

import java.io.Serializable;

/**
 * DTO (используются при запросе) для {@link TypeMeterReading}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeMeterReadingRequestDTO implements Serializable {
    @Size(min = 2, message = "Минимальная длина title должна быть больше 2")
    @Pattern(regexp = "^[А-ЯЁA-Z][а-яёa-z]*$", message = "Title начинается с заглавной буквы и " +
            "содержит только строчные буквы после неё.")
    @NotNull(message = "Title не должен быть null")
    private String title;
}