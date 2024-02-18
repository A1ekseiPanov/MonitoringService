package ru.panov.repository.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.panov.domain.model.Audit;
import ru.panov.repository.AuditRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория журналирования с использованием JDBC.
 */
@Repository
@AllArgsConstructor
public class JdbcAuditRepository implements AuditRepository {
    private final DataSource dataSource;
    private static final String CREATE = """
            INSERT INTO dbo.audit (message)  values (?)""";

    private static final String FIND_ALL = """
            SELECT id,created_at,message FROM dbo.audit""";

    /**
     * Сохраняет запись аудита в базе данных.
     *
     * @param audit запись аудита для сохранения
     * @return сохраненная запись аудита
     */
    @Override
    public Audit save(Audit audit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);

            connection.setAutoCommit(false);
            preparedStatement.setString(1, audit.getMessage());
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            while (key.next()) {
                audit.setId(key.getLong("id"));
            }
            connection.commit();
            return audit;
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
     * Возвращает список всех записей аудита из базы данных.
     *
     * @return список записей аудита
     */
    @Override
    public List<Audit> findAll() {
        List<Audit> audits = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                audits.add(new Audit(resultSet.getLong("id"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        resultSet.getString("message")));
            }
            return audits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}