package repository.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.panov.domain.model.MeterReading;
import ru.panov.repository.MeterReadingRepository;
import util.TestcontainersAbstract;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestData.*;

class JdbcMeterReadingRepositoryTest extends TestcontainersAbstract {
    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Test
    @DisplayName("Тест сохранения показаний счетчиков и получение показаний по ID пользователя")
    void saveAndFindAllByUserIdTest() {
        meterReadingRepository.save(NEW_METER_READING1, USER2.getId());
        meterReadingRepository.save(NEW_METER_READING2, USER2.getId());

        List<MeterReading> userMeterReadings = meterReadingRepository
                .findAllByUserId(USER2.getId());
        assertThat(userMeterReadings).isNotEmpty();
        assertThat(userMeterReadings.size()).isEqualByComparingTo(3);
    }
}