package ru.panov.repository.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.panov.domain.model.TypeMeterReading;
import ru.panov.repository.TypeMeterReadingRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с типами счетчиков в базе данных с использованием JDBC.
 */
@AllArgsConstructor
@Repository
public class JdbcTypeMeterReadingRepository implements TypeMeterReadingRepository {
    private final DataSource dataSource;

    public static final String FIND_ALL_TYPES = "SELECT id, title FROM dbo.type_meter_readings";
    public static final String CREATE_TYPE = "INSERT INTO dbo.type_meter_readings (title) VALUES (?)";
    public static final String FIND_TYPE_BY_ID =
            "SELECT id, title FROM dbo.type_meter_readings WHERE id = ?";
    public static final String FIND_TYPE_BY_TITLE =
            "SELECT id, title FROM dbo.type_meter_readings WHERE title = ?";

    /**
     * Получает все типы показаний счетчиков из базы данных.
     *
     * @return Список объектов TypeMeterReading, представляющих все типы показаний счетчиков.
     * @throws RuntimeException в случае возникновения исключения SQL.
     */
    @Override
    public List<TypeMeterReading> findAll() {
        List<TypeMeterReading> typeMeterReadings = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
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
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(CREATE_TYPE, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            preparedStatement.setString(1, type.getTitle());
            preparedStatement.executeUpdate();
            ResultSet key = preparedStatement.getGeneratedKeys();
            while (key.next()) {
                type.setId(key.getLong("id"));
            }
            connection.commit();
            return type;
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

    /**
     * Находит тип показаний счетчиков по его ID в базе данных.
     *
     * @param id ID типа показаний счетчиков для поиска.
     * @return Optional, содержащий найденный объект TypeMeterReading, или пустой, если не найден.
     * @throws RuntimeException в случае возникновения исключения SQL.
     */
    @Override
    public Optional<TypeMeterReading> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
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

    @Override
    public Optional<TypeMeterReading> findByTitle(String title) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_TYPE_BY_TITLE)) {
            preparedStatement.setString(1, title);
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