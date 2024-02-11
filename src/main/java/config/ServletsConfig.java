package config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import util.ConnectionUtil;
import util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Конфигурация сервлетов, выполняющаяся при запуске приложения.
 */
@WebListener
public class ServletsConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection connection = ConnectionUtil.get()) {
            LiquibaseUtil.update(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}