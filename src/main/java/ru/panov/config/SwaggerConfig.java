package ru.panov.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger.
 */
@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Monitoring Service Api",
                description = """
                        Monitoring Service API
                        <p><b>Тестовые данные:</b><br>
                        - admin: admin / admin<br>
                        - user: user1 / user1<br>
                        - user: user2 / user2</p>
                        """,
                version = "1.0.0",
                contact = @Contact(
                        name = "Aleksey Panov",
                        email = "evil199315@yandex.ru"
                )
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class SwaggerConfig {
}