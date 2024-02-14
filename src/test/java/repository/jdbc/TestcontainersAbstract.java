package repository.jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class TestcontainersAbstract {
    protected static Connection connection;
    protected static JdbcUserRepository userRepository;

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

        userRepository = new JdbcUserRepository();
        LiquibaseUtil.update(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        postgres.stop();
        connection.close();
    }

    @AfterEach
    void afterEach() throws SQLException {

        connection.rollback();
    }
}
