package util;

import entity.MeterReading;
import entity.TypeMeterReading;
import entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestData {
    public static final User USER1 = new User("user1", "user1");
    public static final User USER2 = new User("user2", "user2");
    public static final TypeMeterReading TYPE_METER_READING1= new TypeMeterReading("Холодная вода");
    public static final TypeMeterReading TYPE_METER_READING2= new TypeMeterReading("Горячая вода");
    public static final TypeMeterReading TYPE_METER_READING3= new TypeMeterReading("Отопление");
    public static final MeterReading METER_READING1 = new MeterReading(TYPE_METER_READING1, BigDecimal.valueOf(1), LocalDate.now());
    public static final MeterReading METER_READING2 = new MeterReading(TYPE_METER_READING2, BigDecimal.valueOf(12),LocalDate.of(2022,3,1));
    public static final MeterReading METER_READING3 = new MeterReading(TYPE_METER_READING3, BigDecimal.valueOf(123),LocalDate.of(2021,3,1));
    public static final List<MeterReading> METER_READINGS = List.of(METER_READING1, METER_READING2, METER_READING3);
    public static final int MONTH = 1;
    public static final int YEAR = 2024;
}
