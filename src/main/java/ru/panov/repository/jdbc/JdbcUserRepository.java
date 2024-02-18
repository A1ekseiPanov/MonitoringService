package ru.panov.repository.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.panov.domain.model.User;
import ru.panov.exception.NotFoundException;
import ru.panov.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

/**
 * Реализация репозитория для работы с пользователями в базе данных с использованием JDBC.
 */
@Repository
@AllArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final DataSource dataSource;

    public static final String FIND_USER_BY_ID =
            "SELECT id, username, password, role FROM dbo.users WHERE id = ?";
    public static final String FIND_USER_BY_USERNAME =
            "SELECT id, username, password, role FROM dbo.users WHERE username = ?";
    public static final String SAVE_USER = "INSERT INTO dbo.users (username, password, role) VALUES (?,?,?)";
    public static final String UPDATE_USER =
            "UPDATE dbo.users SET username = ?, password = ? WHERE id = ?";

    /**
     * Поиск пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя
     * @return Объект Optional, содержащий найденного пользователя, или пустой, если пользователь не найден
     */
    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            while (resultSet.next()) {
                user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"));
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Поиск пользователя по его имени пользователя.
     *
     * @param username Имя пользователя
     * @return Объект Optional, содержащий найденного пользователя, или пустой, если пользователь не найден
     */
    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            while (resultSet.next()) {
                user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"));
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохранение пользователя в базе данных.
     *
     * @param user Пользователь для сохранения
     * @return Сохраненный пользователь с присвоенным идентификатором
     */
    @Override
    public User save(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection
                    .prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS);

            connection.setAutoCommit(false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            while (key.next()) {
                user.setId(key.getLong("id"));
            }
            connection.commit();
            return user;
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
     * Обновление информации о пользователе в базе данных.
     *
     * @param id          Идентификатор пользователя
     * @param updatedUser Обновленные данные пользователя
     * @return Обновленный пользователь
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public User update(Long id, User updatedUser) {
        User user = findById(id).orElse(null);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        if (user != null) {
            try {
                connection = dataSource.getConnection();
                preparedStatement = connection.prepareStatement(UPDATE_USER);
                connection.setAutoCommit(false);
                preparedStatement.setString(1, updatedUser.getUsername());
                preparedStatement.setString(2, updatedUser.getPassword());
                preparedStatement.setLong(3, id);
                preparedStatement.executeUpdate();
                connection.commit();
                updatedUser.setId(id);
                return updatedUser;
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
        } else {
            throw new NotFoundException(String.format("Пользователь с id: %s не найден", id));
        }
    }
}