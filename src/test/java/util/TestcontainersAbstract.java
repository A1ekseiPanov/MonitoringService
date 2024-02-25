package util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.panov.MonitoringServiceApp;

@SpringBootTest(classes = MonitoringServiceApp.class)
@ActiveProfiles("test")
public abstract class TestcontainersAbstract {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.7-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @BeforeAll
    static void beforeAll() throws Exception {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}
