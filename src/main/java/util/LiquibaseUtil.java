package util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

/**
 * Утилитный класс для управления миграциями базы данных с помощью Liquibase.
 */
public final class LiquibaseUtil {
    private static final String CHANGELOG_FILE_PATH_KEY = "lb.change_log_file";

    private LiquibaseUtil() {
    }

    /**
     * Обновляет базу данных с помощью Liquibase.
     *
     * @param connection Соединение с базой данных
     * @throws RuntimeException если происходит ошибка при обновлении базы данных
     */
    public static void update(Connection connection) {
        try {Database database = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(PropertiesUtil.get(CHANGELOG_FILE_PATH_KEY),
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции выполнены успешно");
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
