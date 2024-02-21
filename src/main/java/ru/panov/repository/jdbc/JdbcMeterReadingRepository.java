package ru.panov.repository.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.panov.domain.model.MeterReading;
import ru.panov.domain.model.User;
import ru.panov.exception.NotFoundException;
import ru.panov.repository.MeterReadingRepository;
import ru.panov.repository.TypeMeterReadingRepository;
import ru.panov.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для работы с показаниями счетчиков в базе данных с использованием JDBC.
 */
@AllArgsConstructor
@Repository
public class JdbcMeterReadingRepository implements MeterReadingRepository {
    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final TypeMeterReadingRepository typeMeterReadingRepository;

    private static final String FIND_ALL_BY_USER_ID = """
            SELECT id,type_id,reading,local_date,user_id FROM dbo.meter_readings WHERE user_id = ?""";
    private static final String FIND_ALL = """
            SELECT id,type_id,reading,local_date,user_id FROM dbo.meter_readings ORDER BY user_id """;
    private static final String CREATE = """
            INSERT INTO dbo.meter_readings (type_id, reading, local_date, user_id)  values (?,?,?,?)""";

    /**
     * Возвращает список всех показаний счетчиков для пользователя с указанным идентификатором.
     *
     * @param userId Идентификатор пользователя
     * @return Список всех показаний счетчиков для конкретного пользователя или всех показаний для админа
     */
    @Override
    public List<MeterReading> findAllByUserId(Long userId) {
        List<MeterReading> meterReadings = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            return getMeterReadings(meterReadings, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для поиска всех показаний счетчиков с использованием переданного соединения.
     *
     * @return Список всех показаний счетчиков
     * @throws RuntimeException если происходит ошибка при выполнении запроса
     */
    @Override
    public List<MeterReading> findAll() {
        List<MeterReading> meterReadings = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            return getMeterReadings(meterReadings, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MeterReading> getMeterReadings(List<MeterReading> meterReadings,
                                                PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            meterReadings.add(new MeterReading(resultSet.getLong("id"),
                    typeMeterReadingRepository.findById(resultSet.getLong("type_id")).get(),
                    resultSet.getBigDecimal("reading"),
                    resultSet.getDate("local_date").toLocalDate(),
                    userRepository.findById(resultSet.getLong("user_id"))
                            .orElse(null)));
        }
        return meterReadings;
    }

    /**
     * Сохраняет показание счетчика для пользователя с указанным идентификатором.
     *
     * @param meterReading Показание счетчика для сохранения
     * @param userId       Идентификатор пользователя
     * @return Сохраненное показание счетчика
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public MeterReading save(MeterReading meterReading, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id:%s не найден", userId)));
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(CREATE,
                    Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, meterReading.getType().getId());
            preparedStatement.setBigDecimal(2, meterReading.getReading());
            preparedStatement.setDate(3, Date.valueOf(meterReading.getLocalDate()));
            preparedStatement.setLong(4, userId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                meterReading.setId(resultSet.getLong("id"));
            }
            connection.commit();
            meterReading.setUser(user);
            user.getMeterReadings().add(meterReading);
            userRepository.update(user.getId(), user);
            return meterReading;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}