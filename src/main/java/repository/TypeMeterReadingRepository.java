package repository;

import entity.TypeMeterReading;

import java.util.List;
import java.util.Optional;

public interface TypeMeterReadingRepository {
    List<TypeMeterReading> findAll();

    TypeMeterReading save(TypeMeterReading type);

    Optional<TypeMeterReading> findById(Long id);
}
