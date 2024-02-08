package repository.jdbc;

import entity.MeterReading;
import entity.User;
import exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static util.TestData.*;

class JdbcMeterReadingRepositoryTest extends TestcontainersAbstract{
    private static JdbcMeterReadingRepository meterReadingRepository =
            new JdbcMeterReadingRepository(new JdbcUserRepository(),new JdbcTypeMeterReadingRepository());

    @Test
    void saveAndFindAllByUserIdTest() {
        User user = userRepository.save(new User("userrr", "user"), connection);
        meterReadingRepository.save(NEW_METER_READING1, user.getId(), connection);
        meterReadingRepository.save(NEW_METER_READING2, user.getId(), connection);

        List<MeterReading> userMeterReadings = meterReadingRepository
                .findAllByUserId(user.getId(), connection);
        assertThat(userMeterReadings).isNotEmpty();
        assertThat(userMeterReadings.size()).isEqualByComparingTo(2);
    }

    @Test
    void findAllByUserIdUserNotFoundTest() {
        assertThatThrownBy(() ->
                        meterReadingRepository.findAllByUserId(123L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с id:123 не найден");
    }
}