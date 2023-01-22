package ua.org.training.library.web.listeners;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.dao.impl.ConnectionPool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(jakarta.servlet.ServletContextEvent sce) {
        LOGGER.info("Context initialized");
        ApplicationContext.getInstance();
        try {
            LOGGER.info("Initializing connection pool");
            Connection connection = ConnectionPool.getConnection();
            connection.close();
            LOGGER.info("Connection pool initialized");
        } catch (SQLException e) {
            LOGGER.error("Connection pool initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.closePool();
        unregisterDrivers();
        shutdownCleanupThread();
    }

    private static void shutdownCleanupThread() {
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            LOGGER.error("Exception while cleanup thread shutdown", e);
        }
        LOGGER.info("Cleanup thread shutdown");
    }

    private static void unregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.info(String.format("Unregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                LOGGER.warn(String.format("Error unregistering driver %s", driver), e);
            }
        }
    }
}
