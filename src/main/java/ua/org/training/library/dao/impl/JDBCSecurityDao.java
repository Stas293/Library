package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.SecurityDao;
import ua.org.training.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCSecurityDao implements SecurityDao {
    //language=MySQL
    private static final String GET_PASSWORD_BY_LOGIN =
            "SELECT `password` FROM user_list WHERE login = ?";
    private static final Logger LOGGER = LogManager.getLogger(JDBCSecurityDao.class);
    private final Connection connection;

    public JDBCSecurityDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getPasswordByLogin(String login) {
        try (PreparedStatement statement = connection.prepareStatement(GET_PASSWORD_BY_LOGIN)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("Cannot get password by login: %s", login), e);
            throw new DaoException("Cannot get password by login: " + login, e);
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Cannot close connection", e);
            throw new DaoException("Cannot close connection", e);
        }
    }
}
