package repository.jdbc;

import entity.MeterReading;
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

        meterReadingRepository.save(NEW_METER_READING1, USER2.getId(), connection);
        meterReadingRepository.save(NEW_METER_READING2,USER2.getId(), connection);

        List<MeterReading> userMeterReadings = meterReadingRepository
                .findAllByUserId(USER2.getId(), connection);
        assertThat(userMeterReadings).isNotEmpty();
        assertThat(userMeterReadings.size()).isEqualByComparingTo(3);
    }

    @Test
    void findAllByUserIdUserNotFoundTest() {
        assertThatThrownBy(() ->
                        meterReadingRepository.findAllByUserId(123L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с id:123 не найден");
    }
}