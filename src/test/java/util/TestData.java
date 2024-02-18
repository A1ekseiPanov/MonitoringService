package util;

import ru.panov.domain.model.MeterReading;
import ru.panov.domain.model.Role;
import ru.panov.domain.model.TypeMeterReading;
import ru.panov.domain.model.User;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestData {
    public static final User USER1 = new User(2L, "user1", "user1", Role.USER.toString());
    public static final User USER2 = new User(3L, "user2", "user2", Role.USER.toString());
    public static final User ADMIN = new User(1L, "admin", "admin", Role.ADMIN.toString());
    public static final User NEW_USER = new User("user3", "user3");
    public static final User UPDATED_USER = new User("user4", "user4");

    public static final TypeMeterReading TYPE_METER_READING1 = new TypeMeterReading(1L, "Холодная вода");
    public static final TypeMeterReading TYPE_METER_READING2 = new TypeMeterReading(2L, "Горячая вода");
    public static final TypeMeterReading TYPE_METER_READING3 = new TypeMeterReading(3L, "Отопление");
    public static final MeterReading METER_READING1 = new MeterReading(1L, TYPE_METER_READING1, BigDecimal.valueOf(222), LocalDate.now(), USER1);
    public static final MeterReading METER_READING2 = new MeterReading(2L, TYPE_METER_READING2, BigDecimal.valueOf(22234), LocalDate.now(), USER1);
    public static final MeterReading METER_READING3 = new MeterReading(3L, TYPE_METER_READING3, BigDecimal.valueOf(222111), LocalDate.now(), USER1);
    public static final MeterReading METER_READING4 = new MeterReading(4L, TYPE_METER_READING1, BigDecimal.valueOf(333), LocalDate.now(), USER2);
    public static final MeterReading METER_READING5 = new MeterReading(5L, TYPE_METER_READING1, BigDecimal.valueOf(333), LocalDate.of(2024, 1, 4), USER1);
    public static final MeterReading NEW_METER_READING1 = new MeterReading(TYPE_METER_READING2, BigDecimal.valueOf(333), LocalDate.now());
    public static final MeterReading NEW_METER_READING2 = new MeterReading(TYPE_METER_READING3, BigDecimal.valueOf(333), LocalDate.now());

    public static final List<MeterReading> METER_READINGS = List.of(METER_READING1, METER_READING2, METER_READING3, METER_READING4, METER_READING5);
    public static final List<MeterReading> LAST_METER_READINGS = List.of(METER_READING1, METER_READING2, METER_READING3);

    public static final MeterReadingResponseDTO METER_READING1_RESPONSE_DTO = new MeterReadingResponseDTO(1L, TYPE_METER_READING1.getTitle(), BigDecimal.valueOf(222), LocalDate.now());
    public static final List<MeterReadingResponseDTO> METER_READINGS_RESPONSE = List.of(METER_READING1_RESPONSE_DTO);
    public static final int MONTH = 1;
    public static final Long ADMIN_ID = 1L;
    public static final int YEAR = 2024;
}
