package service;

import entity.TypeMeterReading;
import lombok.AllArgsConstructor;
import repository.TypeMeterReadingRepository;
import util.AuditLog;

import java.util.List;

@AllArgsConstructor
public class TypeMeterReadingService {
    private final TypeMeterReadingRepository typeMeterReadingRepository;

    public void addingType(String title) {
        typeMeterReadingRepository.save(new TypeMeterReading(title));
        AuditLog.logAction(String.format("Тип счетчика:\"%s\" добавлен успешно", title));
    }

    public List<TypeMeterReading> getAll() {
        return typeMeterReadingRepository.findAll();
    }
}
