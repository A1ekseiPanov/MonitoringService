package repository.jdbc;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.panov.repository.jdbc.JdbcUserRepository;

import java.sql.Connection;
import java.sql.SQLException;

//@ExtendWith(SpringExtension.class)
public abstract class TestcontainersAbstract {
//    protected static Connection connection;
    protected static JdbcUserRepository userRepository;
    protected static DriverManagerDataSource dataSource;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.7-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void beforeAll() throws Exception {
        postgres.start();
//        connection = DriverManager.getConnection(
//                postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword());
//


        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgres.getDriverClassName());
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        update(dataSource.getConnection());
        userRepository = new JdbcUserRepository(dataSource);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        postgres.stop();
//        dataSource.getConnection().close();
    }

//    @AfterEach
//    void afterEach() throws SQLException {
//
//    }

    private static void update(Connection connection) {
        try {
            Database database = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции выполнены успешно");
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
