package ru.panov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.panov.domain.model.MeterReading;
import ru.panov.domain.model.TypeMeterReading;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;
import ru.panov.service.TypeMeterReadingService;

import java.util.List;

/**
 * Интерфейс-маппер для преобразования объектов MeterReading между различными представлениями.
 */
@Mapper(componentModel = "spring")
public abstract class MeterReadingMapper {
    @Autowired
    private TypeMeterReadingService type;

    /**
     * Метод для преобразования объекта MeterReadingRequestDTO в объект MeterReading.
     *
     * @param dto объект MeterReadingRequestDTO
     * @return объект MeterReading
     */
    public abstract MeterReading requestDTOtoEntity(MeterReadingRequestDTO dto);

    /**
     * Метод для преобразования объекта MeterReading в объект MeterReadingRequestDTO.
     *
     * @param meterReading объект MeterReading
     * @return объект MeterReadingRequestDTO
     */
    public abstract MeterReadingRequestDTO toRequestDTO(MeterReading meterReading);

    /**
     * Метод для преобразования объекта MeterReading в объект MeterReadingResponseDTO.
     *
     * @param meterReading объект MeterReading
     * @return объект MeterReadingResponseDTO
     */
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "typeId", target = "typeMR")
    @Mapping(source = "reading", target = "reading")
    @Mapping(source = "localDate", target = "localDate")
    public MeterReadingResponseDTO toResponseDTO(MeterReading meterReading) {
        TypeMeterReading typeMeterReading = type.getById(meterReading.getTypeId());
        return new MeterReadingResponseDTO(meterReading.getUserId(), typeMeterReading.getTitle(),
                meterReading.getReading(), meterReading.getLocalDate());
    }

    ;

    /**
     * Метод для преобразования списка объектов MeterReading в список объектов MeterReadingResponseDTO.
     *
     * @param meterReadingList список объектов MeterReading
     * @return список объектов MeterReadingResponseDTO
     */
    public abstract List<MeterReadingResponseDTO> toDtoResponseList(List<MeterReading> meterReadingList);
}