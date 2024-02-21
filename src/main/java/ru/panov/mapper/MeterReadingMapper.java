package ru.panov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.panov.domain.model.MeterReading;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;

import java.util.List;

/**
 * Интерфейс-маппер для преобразования объектов MeterReading между различными представлениями.
 */
@Mapper(componentModel = "spring")
public interface MeterReadingMapper {

    /**
     * Метод для преобразования объекта MeterReadingRequestDTO в объект MeterReading.
     *
     * @param dto объект MeterReadingRequestDTO
     * @return объект MeterReading
     */
    @Mapping(source = "typeId", target = "type.id")
    @Mapping(source = "reading", target = "reading")
    MeterReading requestDTOtoEntity(MeterReadingRequestDTO dto);

    /**
     * Метод для преобразования объекта MeterReading в объект MeterReadingRequestDTO.
     *
     * @param meterReading объект MeterReading
     * @return объект MeterReadingRequestDTO
     */
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "reading", target = "reading")
    MeterReadingRequestDTO toRequestDTO(MeterReading meterReading);

    /**
     * Метод для преобразования объекта MeterReading в объект MeterReadingResponseDTO.
     *
     * @param meterReading объект MeterReading
     * @return объект MeterReadingResponseDTO
     */
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "type.title", target = "typeMR")
    @Mapping(source = "reading", target = "reading")
    @Mapping(source = "localDate", target = "localDate")
    MeterReadingResponseDTO toResponseDTO(MeterReading meterReading);

    /**
     * Метод для преобразования списка объектов MeterReading в список объектов MeterReadingResponseDTO.
     *
     * @param meterReadingList список объектов MeterReading
     * @return список объектов MeterReadingResponseDTO
     */
    List<MeterReadingResponseDTO> toDtoResponseList(List<MeterReading> meterReadingList);
}