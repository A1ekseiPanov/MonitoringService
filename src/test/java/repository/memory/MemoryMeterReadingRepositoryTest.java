package repository.memory;

import entity.MeterReading;
import entity.Role;
import entity.TypeMeterReading;
import entity.User;
import exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.MeterReadingRepository;
import repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

import static util.TestData.*;

class MemoryMeterReadingRepositoryTest {
    private MeterReadingRepository meterReadingRepository;
    private UserRepository userRepository;

    @BeforeEach
    void initEach() {
        this.meterReadingRepository = MemoryMeterReadingRepository.getInstance();
        this.userRepository = MemoryUserRepository.getInstance();
    }

    @Test
    void saveAndFindAllByUserIdTest() {
        User user = userRepository.save(USER1);
        meterReadingRepository.save(METER_READING1, user.getId());
        meterReadingRepository.save(METER_READING2, user.getId());

        List<MeterReading> userMeterReadings = meterReadingRepository.findAllMeterReadingByUserId(user.getId());

        Assertions.assertThat(userMeterReadings).isNotEmpty();
        Assertions.assertThat(userMeterReadings).containsExactlyInAnyOrder(METER_READING1, METER_READING2);
        Assertions.assertThat(userMeterReadings.size()).isEqualTo(2);
    }

    @Test
    void findAllByUserIdForAdminTest() {
        User admin = new User("admin", "admin");
        admin.setRole(Role.ADMIN.toString());
        User user = userRepository.save(admin);

        MeterReading meterReading1 = new MeterReading(new TypeMeterReading("Горячая вода"),
                BigDecimal.valueOf(120));
        meterReadingRepository.save(meterReading1, user.getId());

        List<MeterReading> adminMeterReadings = meterReadingRepository.findAllMeterReadingByUserId(user.getId());

        Assertions.assertThat(adminMeterReadings).containsExactly(meterReading1);
    }

    @Test
    void findAllByUserIdUserNotFoundTest() {
        Assertions.assertThatThrownBy(() ->
                        meterReadingRepository.findAllMeterReadingByUserId(123L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с id:123 не найден");
    }
}