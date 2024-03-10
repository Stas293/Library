package ua.org.training.library.connections;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.InjectProperty;

import static ua.org.training.library.enums.DefaultValues.SQL_CONNECTION_INIT;

@Slf4j
@Component
public class ConnectionPool implements AutoCloseable {
    @InjectProperty("postgres.url")
    private String dbUrl;

    @InjectProperty("postgres.username")
    private String dbUsername;

    @InjectProperty("postgres.password")
    private String dbPassword;

    @InjectProperty("postgres.driver")
    private String dbDriver;

    @InjectProperty("postgres.transaction.isolation")
    private String dbTransactionIsolation;

    @InjectProperty("postgres.min.idle")
    private int dbMinIdle;

    @InjectProperty("postgres.max.pool.size")
    private int dbMaxPoolSize;

    @InjectProperty("postgres.max.lifetime")
    private long dbMaxLifetime;

    private final HikariConfig config = new HikariConfig();
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
