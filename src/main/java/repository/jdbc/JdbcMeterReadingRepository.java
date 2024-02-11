package repository.jdbc;

import entity.MeterReading;
import entity.User;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import repository.MeterReadingRepository;
import repository.TypeMeterReadingRepository;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для работы с показаниями счетчиков в базе данных с использованием JDBC.
 */
@AllArgsConstructor
public class JdbcMeterReadingRepository implements MeterReadingRepository {
    private final JdbcUserRepository userRepository;
    private final TypeMeterReadingRepository typeMeterReadingRepository;

    private static final String FIND_ALL_BY_USER_ID = """
            SELECT id,type_id,reading,local_date,user_id FROM dbo.meter_readings WHERE user_id = ?""";
    private static final String FIND_ALL = """
            SELECT id,type_id,reading,local_date,user_id FROM dbo.meter_readings""";
    private static final String CREATE = """
            INSERT INTO dbo.meter_readings (type_id, reading, local_date, user_id)  values (?,?,?,?)""";

    /**
     * Возвращает список всех показаний счетчиков для пользователя с указанным идентификатором.
     *
     * @param userId     Идентификатор пользователя
     * @param connection Соединение с базой данных
     * @return Список всех показаний счетчиков для конкретного пользователя или всех показаний для админа
     * @throws NotFoundException если пользователь не найден
     */
    public List<MeterReading> findAllByUserId(Long userId, Connection connection) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id:%s не найден", userId)));
        List<MeterReading> meterReadings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            return getMeterReadings(connection, meterReadings, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для поиска всех показаний счетчиков с использованием переданного соединения.
     *
     * @param connection Соединение с базой данных
     * @return Список всех показаний счетчиков
     * @throws RuntimeException если происходит ошибка при выполнении запроса
     */
    public List<MeterReading> findAll(Connection connection) {
        List<MeterReading> meterReadings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            return getMeterReadings(connection, meterReadings, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MeterReading> findAll() {
        try (Connection connection = ConnectionUtil.get()) {
            return findAll(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MeterReading> findAllByUserId(Long userId) {
        try (Connection connection = ConnectionUtil.get()) {
            return this.findAllByUserId(userId, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MeterReading> getMeterReadings(Connection connection,
                                                List<MeterReading> meterReadings,
                                                PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            meterReadings.add(new MeterReading(resultSet.getLong("id"),
                    typeMeterReadingRepository.findById(resultSet.getLong("type_id")).get(),
                    resultSet.getBigDecimal("reading"),
                    resultSet.getDate("local_date").toLocalDate(),
                    userRepository.findById(resultSet.getLong("user_id"), connection)
                            .orElse(null)));
        }
        return meterReadings;
    }

    /**
     * Сохраняет показание счетчика для пользователя с указанным идентификатором.
     *
     * @param meterReading Показание счетчика для сохранения
     * @param userId       Идентификатор пользователя
     * @param connection   Соединение с базой данных
     * @return Сохраненное показание счетчика
     * @throws NotFoundException если пользователь не найден
     */
    public MeterReading save(MeterReading meterReading, Long userId, Connection connection) {
        User user = userRepository.findById(userId, connection)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id:%s не найден", userId)));
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE,
                Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, meterReading.getType().getId());
            preparedStatement.setBigDecimal(2, meterReading.getReading());
            preparedStatement.setDate(3, Date.valueOf(meterReading.getLocalDate()));
            preparedStatement.setLong(4, userId);
            preparedStatement.executeUpdate();
            connection.commit();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                meterReading.setId(resultSet.getLong("id"));
            }
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
        }
    }

    @Override
    public MeterReading save(MeterReading meterReading, Long userId) {
        try (Connection connection = ConnectionUtil.get()) {
            return save(meterReading, userId, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}