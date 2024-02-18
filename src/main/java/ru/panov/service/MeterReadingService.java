package ru.panov.service;

import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;

import java.util.List;

/**
 * Интерфейс MeterReadingService определяет методы для работы с показаниями счетчиков.
 */
public interface MeterReadingService {
    List<MeterReadingResponseDTO> getAll(Long userId);

    List<MeterReadingResponseDTO> getLatest(Long userId);

    void submitMeterReading(MeterReadingRequestDTO dto, Long userId);

    List<MeterReadingResponseDTO> getAllMeterReadingsByMonth(int month, int year, Long userId);
}