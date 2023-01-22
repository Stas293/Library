package ua.org.training.library.dao.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static final String MYSQL_URL = Utility.getApplicationProperty("mysql.url");
    private static final String MYSQL_USERNAME = Utility.getApplicationProperty("mysql.username");
    private static final String MYSQL_PASSWORD = Utility.getApplicationProperty("mysql.password");
    private static final String MYSQL_DRIVER = Utility.getApplicationProperty("mysql.driver");
    private static final String MYSQL_TRANSACTION_ISOLATION =
            Utility.getApplicationProperty("mysql.transaction.isolation");
    private static final int MYSQL_MIN_IDLE = Utility.tryParseInt(
            Utility.getApplicationProperty("mysql.min.idle"),
            Constants.DEFAULT_MIN_IDLE);
    private static final int MYSQL_MAX_POOL_SIZE = Utility.tryParseInt(
            Utility.getApplicationProperty("mysql.max.pool.size"),
            Constants.MYSQL_MAX_POOL_SIZE);
    private static final int MYSQL_MAX_LIFETIME = Utility.tryParseInt(
            Utility.getApplicationProperty("mysql.max.lifetime"),
            Constants.DEFAULT_MAX_LIFETIME);
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl(MYSQL_URL);
        config.setUsername(MYSQL_USERNAME);
        config.setPassword(MYSQL_PASSWORD);
        config.setDriverClassName(MYSQL_DRIVER);
        config.setTransactionIsolation(MYSQL_TRANSACTION_ISOLATION);
        config.setMinimumIdle(MYSQL_MIN_IDLE);
        config.setMaximumPoolSize(MYSQL_MAX_POOL_SIZE);
        config.setMaxLifetime(MYSQL_MAX_LIFETIME);
        config.setConnectionInitSql("SELECT 1");
        LOGGER.info("Connection pool configuration: {}", config);
        ds = new HikariDataSource(config);
    }

    private ConnectionPool() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void closePool() {
        ds.close();
        LOGGER.info("Connection pool closed");
    }
}
