package repository.jdbc;

import entity.MeterReading;
import entity.Role;
import entity.User;
import exception.NotFoundException;
import repository.MeterReadingRepository;
import repository.TypeMeterReadingRepository;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с показаниями счетчиков в базе данных с использованием JDBC.
 */
public class JdbcMeterReadingRepository implements MeterReadingRepository {
    private final JdbcUserRepository userRepository;

    private static final String FIND_ALL_BY_USER_ID = """
            SELECT id,type_id,reading,local_date,user_id FROM meter_readings WHERE user_id = ?""";
    private static final String FIND_ALL = """
            SELECT id,type_id,reading,local_date,user_id FROM meter_readings""";
    private static final String CREATE = """
            INSERT INTO meter_readings (type_id, reading, local_date, user_id)  values (?,?,?,?)""";
    private final TypeMeterReadingRepository typeMeterReadingRepository;

    private static JdbcMeterReadingRepository INSTANCE;

    private JdbcMeterReadingRepository(JdbcUserRepository userRepository) {
        this.userRepository = userRepository;
        this.typeMeterReadingRepository = JdbcTypeMeterReadingRepository.getInstance();
    }

    public static JdbcMeterReadingRepository getInstance(JdbcUserRepository userRepository) {
        if (INSTANCE == null) {
            INSTANCE = new JdbcMeterReadingRepository(userRepository);
        }
        return INSTANCE;
    }

    /**
     * Возвращает список всех показаний счетчиков для пользователя с указанным идентификатором.
     *
     * @param userId     Идентификатор пользователя
     * @param connection Соединение с базой данных
     * @return Список всех показаний счетчиков для конкретного пользователя или всех показаний для админа
     * @throws NotFoundException если пользователь не найден
     */
    public List<MeterReading> findAllMeterReadingByUserId(Long userId, Connection connection) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id:%s не найден", userId));
        }
        List<MeterReading> meterReadings = new ArrayList<>();
        if (!user.get().getRole().equalsIgnoreCase(Role.ADMIN.toString())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID)) {
                preparedStatement.setLong(1, userId);
                return getMeterReadings(connection, meterReadings, preparedStatement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
                return getMeterReadings(connection, meterReadings, preparedStatement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<MeterReading> findAllMeterReadingByUserId(Long userId) {
        try (Connection connection = ConnectionUtil.get()) {
            return findAllMeterReadingByUserId(userId, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MeterReading> getMeterReadings(Connection connection, List<MeterReading> meterReadings, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            meterReadings.add(new MeterReading(resultSet.getLong("id"),
                    typeMeterReadingRepository.findById(resultSet.getLong("type_id")).get(),
                    resultSet.getBigDecimal("reading"),
                    resultSet.getDate("local_date").toLocalDate(),
                    userRepository.findById(resultSet.getLong("user_id"), connection).orElse(null)));
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
        Optional<User> user = userRepository.findById(userId, connection);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id:%s не найден", userId));
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, meterReading.getType().getId());
            preparedStatement.setBigDecimal(2, meterReading.getReading());
            preparedStatement.setDate(3, Date.valueOf(meterReading.getLocalDate()));
            preparedStatement.setLong(4, userId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                meterReading.setId(resultSet.getLong("id"));
            }
            meterReading.setUser(user.get());
            user.get().getMeterReadings().add(meterReading);
            userRepository.update(user.get().getId(), user.get());
            return meterReading;
        } catch (SQLException e) {
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

