package repository.jdbc;

import entity.MeterReading;
import entity.Role;
import entity.TypeMeterReading;
import entity.User;
import exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.MeterReadingRepository;
import repository.UserRepository;
import repository.memory.MemoryMeterReadingRepository;
import repository.memory.MemoryUserRepository;
import util.LiquibaseUtil;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static util.TestData.*;

class JdbcMeterReadingRepositoryTest {
    private static Connection connection;
    private static JdbcMeterReadingRepository meterReadingRepository;
    private static JdbcUserRepository userRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.7-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgres.start();
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());

        userRepository = JdbcUserRepository.getInstance();
        meterReadingRepository = JdbcMeterReadingRepository.getInstance(JdbcUserRepository.getInstance());
        LiquibaseUtil.update(connection);
    }


    @Test
    void saveAndFindAllByUserIdTest() {
        User user = userRepository.save(new User("userrr", "user"), connection);
        meterReadingRepository.save(NEW_METER_READING1, user.getId(), connection);
        meterReadingRepository.save(NEW_METER_READING2, user.getId(), connection);

        List<MeterReading> userMeterReadings = meterReadingRepository
                .findAllMeterReadingByUserId(user.getId(), connection);
        assertThat(userMeterReadings).isNotEmpty();
        assertThat(userMeterReadings.size()).isEqualByComparingTo(2);

    }

    @Test
    void findAllByUserIdForAdminTest() {
        List<MeterReading> adminMeterReadings = meterReadingRepository
                .findAllMeterReadingByUserId(ADMIN_ID, connection);
        assertThat(adminMeterReadings).isNotEmpty();
    }

    @Test
    void findAllByUserIdUserNotFoundTest() {
        assertThatThrownBy(() ->
                        meterReadingRepository.findAllMeterReadingByUserId(123L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с id:123 не найден");
    }
}