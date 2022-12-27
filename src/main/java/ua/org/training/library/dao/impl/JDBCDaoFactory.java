package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.*;
import ua.org.training.library.exceptions.JDBCException;

import java.sql.SQLException;

import static ua.org.training.library.dao.impl.ConnectionPool.getConnection;

public class JDBCDaoFactory extends DaoFactory {
    private static final Logger LOGGER = LogManager.getLogger(JDBCDaoFactory.class);

    @Override
    public AuthorDao createAuthorDao() throws JDBCException {
        try {
            return new JDBCAuthorDao(getConnection());
        } catch (SQLException e) {
            LOGGER.error("Cannot create AuthorDao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public BookDao createBookDao() throws JDBCException {
        try {
            return new JDBCBookDao(getConnection());
        } catch (SQLException e) {
            LOGGER.error("Cannot create BookDao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public HistoryOrderDao createHistoryOrderDao() throws JDBCException {
        try {
            return new JDBCHistoryOrderDao(getConnection());
        } catch (SQLException e) {
            LOGGER.error("Cannot create HistoryOrderDao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public OrderDao createOrderDao() throws JDBCException {
        try {
            return new JDBCOrderDao(getConnection());
        } catch (SQLException e) {
            LOGGER.error("Cannot create OrderDao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public PlaceDao createPlaceDao() throws JDBCException {
        try {
            return new JDBCPlaceDao(getConnection());
        } catch (SQLException e) {
            LOGGER.error("Cannot create PlaceDao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public RoleDao createRoleDao() throws JDBCException {
        try {
            return new JDBCRoleDao(getConnection());
        } catch (SQLException e) {
            LOGGER.error("Cannot create RoleDao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public UserDao createUserDao() throws JDBCException {
        try {
            return new JDBCUserDao(getConnection());
        } catch (Exception e) {
            LOGGER.error("Cannot create user dao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public SecurityDao createSecurityDao() throws JDBCException {
        try {
            return new JDBCSecurityDao(getConnection());
        } catch (Exception e) {
            LOGGER.error("Cannot create security dao", e);
            throw new JDBCException(e);
        }
    }

    @Override
    public StatusDao createStatusDao() throws JDBCException {
        try {
            return new JDBCStatusDao(getConnection());
        } catch (Exception e) {
            LOGGER.error("Cannot create status dao", e);
            throw new JDBCException(e);
        }
    }
}
