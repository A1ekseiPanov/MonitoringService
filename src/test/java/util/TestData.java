package util;

import entity.MeterReading;
import entity.TypeMeterReading;
import entity.User;

import java.math.BigDecimal;

public class TestData {
    public static User USER1 = new User("user1", "user1");
    public static User USER2 = new User("user2", "user2");
    public static MeterReading METER_READING = new MeterReading(TypeMeterReading.COLD_WATER, BigDecimal.valueOf(1234));
}
