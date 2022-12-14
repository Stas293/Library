package ua.org.training.library.dao.impl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final DataSource ds;

    static {
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            ds = (DataSource)envContext.lookup("jdbc/someDB");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionPool() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
