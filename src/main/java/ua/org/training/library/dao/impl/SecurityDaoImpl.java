package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.SecurityDao;
import ua.org.training.library.enums.constants.SecurityQueries;
import ua.org.training.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class SecurityDaoImpl implements SecurityDao {
    private final SecurityQueries securityQueries;

    @Autowired
    public SecurityDaoImpl(SecurityQueries securityQueries) {
        this.securityQueries = securityQueries;
    }

    @Override
    public String getPasswordByLogin(Connection connection, String username) {
        log.info("Getting password by login: {}", username);
        try (PreparedStatement statement = connection.prepareStatement(
                securityQueries.getGetPasswordByLoginQuery())) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("password");
                }
                log.error("No password found by login: {}", username);
                throw new DaoException("No password found by login: " + username);
            }
        } catch (SQLException e) {
            log.error("Error getting password by login: {}", username, e);
            throw new DaoException("Error getting password by login: " + username, e);
        }
    }
}
