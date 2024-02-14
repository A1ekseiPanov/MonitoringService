package repository.jdbc;

import entity.TypeMeterReading;
import repository.TypeMeterReadingRepository;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с типами счетчиков в базе данных с использованием JDBC.
 */
public class JdbcTypeMeterReadingRepository implements TypeMeterReadingRepository {

    public static final String FIND_ALL_TYPES = "SELECT id, title FROM dbo.type_meter_readings";
    public static final String CREATE_TYPE = "INSERT INTO dbo.type_meter_readings (title) VALUES (?)";
    public static final String FIND_TYPE_BY_ID =
            "SELECT id, title FROM dbo.type_meter_readings WHERE id = ?";

    /**
     * Получает все типы показаний счетчиков из базы данных.
     *
     * @return Список объектов TypeMeterReading, представляющих все типы показаний счетчиков.
     * @throws RuntimeException в случае возникновения исключения SQL.
     */
    @Override
    public List<TypeMeterReading> findAll() {
        List<TypeMeterReading> typeMeterReadings = new ArrayList<>();
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement p = connection.prepareStatement(FIND_ALL_TYPES)) {
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()) {
                typeMeterReadings.add(
                        new TypeMeterReading(resultSet.getLong("id"),
                                resultSet.getString("title")));
            }
            return typeMeterReadings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохраняет новый тип показаний счетчиков в базе данных.
     *
     * @param type Объект TypeMeterReading для сохранения.
     * @return Сохраненный объект TypeMeterReading с установленным идентификатором.
     * @throws RuntimeException в случае возникновения исключения SQL.
     */
    @Override
    public TypeMeterReading save(TypeMeterReading type) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtil.get();
            preparedStatement = connection.prepareStatement(CREATE_TYPE, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            preparedStatement.setString(1, type.getTitle());
            preparedStatement.executeUpdate();
            connection.commit();
            ResultSet key = preparedStatement.getGeneratedKeys();
            while (key.next()) {
                type.setId(key.getLong("id"));
            }
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException(e);
            }
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return type;
    }

    /**
     * Находит тип показаний счетчиков по его ID в базе данных.
     *
     * @param id ID типа показаний счетчиков для поиска.
     * @return Optional, содержащий найденный объект TypeMeterReading, или пустой, если не найден.
     * @throws RuntimeException в случае возникновения исключения SQL.
     */
    @Override
    public Optional<TypeMeterReading> findById(Long id) {
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_TYPE_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            TypeMeterReading typeMeterReading = null;
            while (resultSet.next()) {
                typeMeterReading = new TypeMeterReading(
                        resultSet.getLong("id"),
                        resultSet.getString("title")
                );
            }
            return Optional.ofNullable(typeMeterReading);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}