package repository;

import entity.MeterReading;

import java.util.List;

/**
 * Интерфейс MeterReadingRepository определяет методы для работы с показаниями счетчиков.
 */
public interface MeterReadingRepository {
    List<MeterReading> findAllByUserId(Long userId);

    List<MeterReading> findAll();

    MeterReading save(MeterReading meterReading, Long userId);
}