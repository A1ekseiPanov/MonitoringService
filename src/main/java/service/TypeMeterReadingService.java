package service;

import entity.TypeMeterReading;
import repository.TypeMeterReadingRepository;
import repository.jdbc.JdbcTypeMeterReadingRepository;
import util.AuditLog;

import java.util.List;

public class TypeMeterReadingService {
    private TypeMeterReadingRepository typeMeterReadingRepository;
    private static final TypeMeterReadingService INSTANCE = new TypeMeterReadingService();

    private TypeMeterReadingService() {
        this.typeMeterReadingRepository = JdbcTypeMeterReadingRepository.getInstance();
    }

    public static TypeMeterReadingService getInstance() {
        return INSTANCE;
    }

    public void addingType(String title) {
        typeMeterReadingRepository.save(new TypeMeterReading(title));
        AuditLog.logAction(String.format("Тип счетчика:\"%s\" добавлен успешно", title));
    }

    public List<TypeMeterReading> getAll() {
        return typeMeterReadingRepository.findAll();
    }
}
