package ua.org.training.library.connections;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.InjectProperty;

import static ua.org.training.library.enums.DefaultValues.SQL_CONNECTION_INIT;

@Slf4j
@Component
public class ConnectionPool implements AutoCloseable {
    private final HikariConfig config = new HikariConfig();
    @NotBlank(message = "Database URL must not be blank")
    @InjectProperty("postgres.url")
    private String dbUrl;
    @NotBlank(message = "Database username must not be blank")
    @InjectProperty("postgres.username")
    private String dbUsername;
    @NotBlank(message = "Database password must not be blank")
    @InjectProperty("postgres.password")
    private String dbPassword;
    @NotBlank(message = "Database driver must not be blank")
    @InjectProperty("postgres.driver")
    private String dbDriver;
    @NotBlank(message = "Database transaction isolation must not be blank")
    @InjectProperty("postgres.transaction.isolation")
    private String dbTransactionIsolation;
    @Min(value = 1, message = "Database minimum idle connections must be greater than 0")
    @InjectProperty("postgres.min.idle")
    private int dbMinIdle;
    @Min(value = 1, message = "Database maximum pool size must be greater than 0")
    @InjectProperty("postgres.max.pool.size")
    private int dbMaxPoolSize;
    @Min(value = 1, message = "Database maximum lifetime must be greater than 0")
    @InjectProperty("postgres.max.lifetime")
    private long dbMaxLifetime;
    @Delegate
    private HikariDataSource ds;

    @PostConstruct
    public void init() {
        log.info("Creating connection pool with PostConstruct");
        log.info("Setting connection pool configuration");
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setDriverClassName(dbDriver);
        config.setTransactionIsolation(dbTransactionIsolation);
        config.setMinimumIdle(dbMinIdle);
        config.setMaximumPoolSize(dbMaxPoolSize);
        config.setMaxLifetime(dbMaxLifetime);
        config.setConnectionInitSql(SQL_CONNECTION_INIT.getValue());
        ds = new HikariDataSource(config);
        log.info("Connection pool created");
    }
}
