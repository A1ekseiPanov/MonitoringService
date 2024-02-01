package repository;

import entity.TypeMeterReading;

import java.util.List;

public interface TypeMeterReadingRepository {
    List<TypeMeterReading> findAll();

    TypeMeterReading save(TypeMeterReading type);
}
