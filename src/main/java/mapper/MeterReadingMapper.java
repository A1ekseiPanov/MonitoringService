package mapper;

import repository.TypeMeterReadingRepository;
import repository.jdbc.JdbcTypeMeterReadingRepository;
import entity.dto.MeterReadingDto;
import entity.MeterReading;
import entity.TypeMeterReading;
import service.TypeMeterReadingService;


public class MeterReadingMapper {
    private TypeMeterReadingRepository tmrr;
    private TypeMeterReadingService tmr;
    private static final MeterReadingMapper INSTANCE = new MeterReadingMapper();

    private MeterReadingMapper() {
        this.tmrr = new JdbcTypeMeterReadingRepository();
        this.tmr = new TypeMeterReadingService(tmrr);
    }

    public static MeterReadingMapper getInstance() {
        return INSTANCE;
    }

    public MeterReading meterReadingDtoToMeterReading(MeterReadingDto dto) {
        if (dto == null) {
            return null;
        }
        return new MeterReading(fromLong(dto.getTypeId()), dto.getReading());
    }

    private TypeMeterReading fromLong(Long id) {
        return id == null ? null : tmr.getById(id);
    }
}