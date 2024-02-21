package ru.panov.repository;

import ru.panov.domain.model.TypeMeterReading;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс TypeMeterReadingRepository определяет методы для работы с типами показаний счетчиков.
 */
public interface TypeMeterReadingRepository {
    List<TypeMeterReading> findAll();

    TypeMeterReading save(TypeMeterReading type);

    Optional<TypeMeterReading> findById(Long id);

    Optional<TypeMeterReading> findByTitle(String title);
}