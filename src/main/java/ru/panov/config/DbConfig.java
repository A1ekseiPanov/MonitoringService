package ru.panov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * Конфигурация базы данных.
 */
@Configuration
public class DbConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dSB = DataSourceBuilder.create();
        dSB.driverClassName(driverClassName);
        dSB.url(url);
        dSB.username(username);
        dSB.password(password);
        return dSB.build();
    }
}