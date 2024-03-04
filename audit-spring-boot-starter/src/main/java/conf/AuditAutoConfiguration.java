package conf;

import aspect.AuditAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import repository.AuditRepository;
import repository.JdbcAuditRepository;
import service.AuditService;
import service.AuditServiceImpl;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class AuditAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        return new DriverManagerDataSource();
    }

    @Bean
    public AuditRepository auditRepository() {
        return new JdbcAuditRepository(dataSource());
    }

    @Bean
    public AuditService auditService() {
        return new AuditServiceImpl(auditRepository());
    }

    @Bean
    public AuditAspect aspect() {
        return new AuditAspect(auditService());
    }

    @PostConstruct
    public void init() {
        log.info("AuditAutoConfiguration init");
    }
}