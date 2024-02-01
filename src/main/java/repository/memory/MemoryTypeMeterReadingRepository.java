package repository.memory;

import entity.TypeMeterReading;
import repository.TypeMeterReadingRepository;

import java.util.ArrayList;
import java.util.List;

public class MemoryTypeMeterReadingRepository implements TypeMeterReadingRepository {
    private Long id = 0L;
    private List<TypeMeterReading> types = new ArrayList<>();
    private static final MemoryTypeMeterReadingRepository INSTANCE = new MemoryTypeMeterReadingRepository();

    private MemoryTypeMeterReadingRepository() {
    }

    {
        save(new TypeMeterReading("Холодная вода"));
        save(new TypeMeterReading("Горячая вода"));
        save(new TypeMeterReading("Отопление"));
    }

    public static MemoryTypeMeterReadingRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<TypeMeterReading> findAll() {
        return types;
    }

    @Override
    public TypeMeterReading save(TypeMeterReading type) {
        if (type.getId() == null) {
            type.setId(++id);
        }
        types.add(type);
        return type;
    }
}
