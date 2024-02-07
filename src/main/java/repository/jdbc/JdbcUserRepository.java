package repository.jdbc;

import entity.User;
import exception.NotFoundException;
import repository.UserRepository;
import util.ConnectionUtil;

import java.sql.*;
import java.util.Optional;

/**
 * Реализация репозитория для работы с пользователями в базе данных с использованием JDBC.
 */
public class JdbcUserRepository implements UserRepository {
    private static JdbcUserRepository INSTANCE = new JdbcUserRepository();
    public static final String FIND_USER_BY_ID =
            "SELECT id, username, password, role FROM users WHERE id = ?";
    public static final String FIND_USER_BY_USERNAME =
            "SELECT id, username, password, role FROM users WHERE username = ?";
    public static final String SAVE_USER = "INSERT INTO users (username, password, role) VALUES (?,?,?)";
    public static final String UPDATE_USER =
            "UPDATE users SET username = ?, password = ? WHERE id = ?";

    private JdbcUserRepository() {
    }

    public static JdbcUserRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Поиск пользователя по его идентификатору.
     *
     * @param id         Идентификатор пользователя
     * @param connection Соединение с базой данных
     * @return Объект Optional, содержащий найденного пользователя, или пустой, если пользователь не найден
     */
    public Optional<User> findById(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {
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

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = ConnectionUtil.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Поиск пользователя по его имени пользователя.
     *
     * @param username   Имя пользователя
     * @param connection Соединение с базой данных
     * @return Объект Optional, содержащий найденного пользователя, или пустой, если пользователь не найден
     */
    public Optional<User> findByUsername(String username, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)) {
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

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = ConnectionUtil.get()) {
            return findByUsername(username, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохранение пользователя в базе данных.
     *
     * @param user       Пользователь для сохранения
     * @param connection Соединение с базой данных
     * @return Сохраненный пользователь с присвоенным идентификатором
     */
    public User save(User user, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            while (key.next()) {
                user.setId(key.getLong("id"));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        try (Connection connection = ConnectionUtil.get()) {
            return save(user, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Обновление информации о пользователе в базе данных.
     *
     * @param id          Идентификатор пользователя
     * @param updatedUser Обновленные данные пользователя
     * @param connection  Соединение с базой данных
     * @return Обновленный пользователь
     * @throws NotFoundException если пользователь не найден
     */
    public User update(Long id, User updatedUser, Connection connection) {
        User user = findById(id, connection).orElse(null);
        if (user != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
                preparedStatement.setString(1, updatedUser.getUsername());
                preparedStatement.setString(2, updatedUser.getPassword());
                preparedStatement.setLong(3, id);
                preparedStatement.executeUpdate();
                updatedUser.setId(id);
                return updatedUser;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с id: %s не найден", id));
        }
    }

    @Override
    public User update(Long id, User updatedUser) {
        try (Connection connection = ConnectionUtil.get()) {
            return update(id, updatedUser, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
