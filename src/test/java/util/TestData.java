package util;

import ru.panov.domain.model.MeterReading;
import ru.panov.domain.model.Role;
import ru.panov.domain.model.TypeMeterReading;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.requestDTO.TypeMeterReadingRequestDTO;
import ru.panov.domain.requestDTO.UserRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class TestData {
    public static final User USER1 = new User(2L, "user1", "$2a$10$GpyHA0hAmcnxO9hKRFU.MOsuQ1iWAqprvVYXuqOdfcX6vJ935oj4W", Role.USER.toString());
    public static final User USER2 = new User(3L, "user2", "$2a$10$8ldAWPlW2kSQgUm3Qc2q9ey70Okxu54mD41ooielCIauvsENKgeQu", Role.USER.toString());
    public static final User ADMIN = new User(1L, "admin", "$2a$10$HEsPp2SwZnAOW9m.PVjSkep0yfQU8ZcF7sI65LHV7.9na97SXIapW", Role.ADMIN.toString());
    public static final User NEW_USER = new User("user3", "user3");
    public static final User UPDATED_USER = new User("user4", "user4");

    public static final TypeMeterReading TYPE_METER_READING1 = new TypeMeterReading(1L, "Холодная вода");
    public static final TypeMeterReading TYPE_METER_READING2 = new TypeMeterReading(2L, "Горячая вода");
    public static final TypeMeterReading TYPE_METER_READING3 = new TypeMeterReading(3L, "Отопление");

    public static final TypeMeterReadingRequestDTO NEW_TYPE_METER_READING_REQUEST_DTO = new TypeMeterReadingRequestDTO("Газ");

    public static final MeterReading METER_READING1 = new MeterReading(1L, TYPE_METER_READING1.getId(), BigDecimal.valueOf(222), LocalDate.now(), USER1.getId());
    public static final MeterReading METER_READING2 = new MeterReading(2L, TYPE_METER_READING2.getId(), BigDecimal.valueOf(22234), LocalDate.now(), USER1.getId());
    public static final MeterReading METER_READING3 = new MeterReading(3L, TYPE_METER_READING3.getId(), BigDecimal.valueOf(222111), LocalDate.now(), USER1.getId());
    public static final MeterReading METER_READING4 = new MeterReading(4L, TYPE_METER_READING1.getId(), BigDecimal.valueOf(333), LocalDate.now(), USER2.getId());
    public static final MeterReading METER_READING5 = new MeterReading(5L, TYPE_METER_READING1.getId(), BigDecimal.valueOf(333), LocalDate.of(2024, 1, 4), USER1.getId());

    public static final MeterReading NEW_METER_READING1 = new MeterReading(TYPE_METER_READING2.getId(), BigDecimal.valueOf(333), LocalDate.now());
    public static final MeterReading NEW_METER_READING2 = new MeterReading(TYPE_METER_READING3.getId(), BigDecimal.valueOf(333), LocalDate.now());

    public static final List<MeterReading> METER_READINGS = List.of(METER_READING1, METER_READING2, METER_READING3, METER_READING4, METER_READING5);

    public static final MeterReadingRequestDTO METER_READING1_REQUEST_DTO = new MeterReadingRequestDTO(TYPE_METER_READING2.getId(), BigDecimal.valueOf(222));

    public static final UserRequestDTO USER_REQUEST_DTO = new UserRequestDTO(USER1.getUsername(), USER1.getPassword());

    public static final UserRequestDTO NEW_USER_REQUEST_DTO = new UserRequestDTO("Asdfg", "Asdfg");
}
