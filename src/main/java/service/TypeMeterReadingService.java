package service;

import annotations.Audit;
import repository.TypeMeterReadingRepository;
import entity.TypeMeterReading;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import util.AuditLog;

import java.util.List;

@AllArgsConstructor
@Audit
public class TypeMeterReadingService {
    private final TypeMeterReadingRepository typeMeterReadingRepository;

    public void addingType(String title) {
        typeMeterReadingRepository.save(new TypeMeterReading(title));
        AuditLog.logAction(String.format("Тип счетчика:%s добавлен успешно", title));
    }

    public List<TypeMeterReading> getAll() {
        return typeMeterReadingRepository.findAll();
    }

    public TypeMeterReading getById(Long id) {
        return typeMeterReadingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Тип счетчика (id=%s) отсутствует", id)));
    }
}
