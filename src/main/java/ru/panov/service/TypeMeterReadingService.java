package ru.panov.service;

import ru.panov.domain.model.TypeMeterReading;
import ru.panov.domain.requestDTO.TypeMeterReadingRequestDTO;
import ru.panov.domain.responseDTO.TypeMeterReadingResponseDTO;

import java.util.List;

/**
 * Интерфейс TypeMeterReadingService определяет методы для работы с типами показаний счетчиков.
 */
public interface TypeMeterReadingService {
    void addingType(TypeMeterReadingRequestDTO type);

    List<TypeMeterReadingResponseDTO> getAll();

    TypeMeterReading getById(Long id);
}