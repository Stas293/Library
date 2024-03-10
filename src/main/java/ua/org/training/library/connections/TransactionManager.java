package ua.org.training.library.connections;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.exceptions.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

@Component
@Slf4j
public class TransactionManager {
    private final ThreadLocal<Connection> connectionHolder;
    private final ConnectionPool connectionPool;

    @Autowired
    public TransactionManager(ConnectionPool connectionPool) {
        this.connectionHolder = new ThreadLocal<>();
        this.connectionPool = connectionPool;
    }

    public void beginTransaction() {
        log.info("Begin transaction");
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);
            connectionHolder.set(conn);
        } catch (SQLException ex) {
            log.error("Error while begin transaction", ex);
            throw new TransactionException("Error starting transaction", ex);
        }
    }

    public void commitTransaction() {
        log.info("Commit transaction");
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                log.error("Error while commit transaction", ex);
                throw new TransactionException("Error committing transaction", ex);
            } finally {
                closeConnection();
            }
        }
    }

    public void rollbackTransaction() {
        log.info("Rollback transaction");
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                log.error("Error while rollback transaction", ex);
                throw new TransactionException("Error rollback transaction", ex);
            } finally {
                closeConnection();
            }
        }
    }

    public Connection getConnection() {
        log.info("Get connection");
        Connection conn = connectionHolder.get();
        if (conn == null) {
            log.info("Connection is null");
            try {
                conn = connectionPool.getConnection();
                connectionHolder.set(conn);
            } catch (SQLException ex) {
                log.error("Error while getting connection", ex);
                throw new TransactionException("Error getting connection", ex);
            }
        }
        return conn;
    }

    public void closeConnection() {
        try (Connection conn = connectionHolder.get()) {
            log.info("Closing connection");
            connectionHolder.remove();
        } catch (SQLException ex) {
            log.error("Error while closing connection", ex);
            throw new TransactionException("Error closing connection", ex);
        }
    }
}

