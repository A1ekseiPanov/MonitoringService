package ru.panov.mapper;

import org.mapstruct.Mapper;
import ru.panov.domain.model.TypeMeterReading;
import ru.panov.domain.requestDTO.TypeMeterReadingRequestDTO;
import ru.panov.domain.responseDTO.TypeMeterReadingResponseDTO;

import java.util.List;

/**
 * Интерфейс-маппер для преобразования объектов TypeMeterReading между различными представлениями.
 */
@Mapper(componentModel = "spring")
public interface TypeMapper {

    /**
     * Метод для преобразования объекта TypeMeterReadingRequestDTO в объект TypeMeterReading.
     *
     * @param dto объект TypeMeterReadingRequestDTO
     * @return объект TypeMeterReading
     */
    TypeMeterReading requestDtoToEntity(TypeMeterReadingRequestDTO dto);

    /**
     * Метод для преобразования списка объектов TypeMeterReading в список объектов TypeMeterReadingResponseDTO.
     *
     * @param typeMeterReading список объектов TypeMeterReading
     * @return список объектов TypeMeterReadingResponseDTO
     */
    List<TypeMeterReadingResponseDTO> mapEntityToResponseDto(List<TypeMeterReading> typeMeterReading);

    /**
     * Метод для преобразования объекта TypeMeterReading в объект TypeMeterReadingResponseDTO.
     *
     * @param tmr объект TypeMeterReading
     * @return объект TypeMeterReadingResponseDTO
     */
    TypeMeterReadingResponseDTO entityToToResponseDto(TypeMeterReading tmr);
}