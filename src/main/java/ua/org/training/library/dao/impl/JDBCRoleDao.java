package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.dao.collector.RoleCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Role;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCRoleDao implements RoleDao {
    //language=MySQL
    private static final String INSERT_ROLE = "INSERT INTO `role` (`code`, `name`) VALUES (?, ?)";
    //language=MySQL
    private static final String GET_ROLE_BY_ID = "SELECT * FROM `role` WHERE id = ?";
    //language=MySQL
    private static final String GET_ALL_ROLES = "SELECT * FROM `role`";
    //language=MySQL
    private static final String UPDATE_ROLE = "UPDATE `role` SET `code` = ?, `name` = ? WHERE id = ?";
    //language=MySQL
    private static final String DELETE_ROLE = "DELETE FROM `role` WHERE id = ?";
    //language=MySQL
    private static final String DELETE_USER_ROLE = "DELETE FROM `user_role` WHERE role_id = ?";
    //language=MySQL
    private static final String GET_ROLE_BY_CODE = "SELECT * FROM `role` WHERE `code` = ?";
    //language=MySQL
    private static final String GET_ROLE_BY_NAME = "SELECT * FROM `role` WHERE `name` = ?";
    //language=MySQL
    private static final String GET_ROLES_BY_USER_ID = "SELECT `role`.* FROM `role` inner join " +
            "user_role ur on `role`.id = ur.role_id " +
            "where ur.user_id = ?";
    //language=MySQL
    private static final String GET_ROLES_BY_USER_LOGIN = "SELECT `role`.* FROM `role` inner join " +
            "user_role ur on `role`.id = ur.role_id " +
            "inner join user_list ul on ur.user_id = ul.id " +
            "where ul.login = ?";
    private static final Logger LOGGER = LogManager.getLogger(JDBCRoleDao.class);
    private final Connection connection;

    public JDBCRoleDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long create(Role model) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(INSERT_ROLE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getCode());
            statement.setString(2, model.getName());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                connection.commit();
                return id;
            }
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't create role", e);
            throw new DaoException("Can't create role", e);
        } finally {
            connection.setAutoCommit(true);
        }
        return 0;
    }

    @Override
    public Optional<Role> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_ROLE_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new RoleCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get role by id", e);
            throw new DaoException("Can't get role by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Role> getPage(Page<Role> page) {
        List<Role> roles = getAllRoles();
        page.setData(roles);
        page.setElementsCount(roles.size());
        return page;
    }

    @Override
    public void update(Role entity) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ROLE)) {
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getName());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't update role", e);
            throw new DaoException("Can't update role", e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ROLE)) {
            statement.setLong(1, id);
            try (PreparedStatement statement1 = connection.prepareStatement(DELETE_USER_ROLE)) {
                statement1.setLong(1, id);
                statement1.executeUpdate();
            }
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't delete role", e);
            throw new DaoException("Can't delete role", e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Can't close connection", e);
            throw new DaoException("Can't close connection", e);
        }
    }

    @Override
    public Optional<Role> getByCode(String code) {
        try (PreparedStatement statement = connection.prepareStatement(GET_ROLE_BY_CODE)) {
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new RoleCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get role by code", e);
            throw new DaoException("Can't get role by code", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> getByName(String name) {
        try (PreparedStatement statement = connection.prepareStatement(GET_ROLE_BY_NAME)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new RoleCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get role by name", e);
            throw new DaoException("Can't get role by name", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Role> getRolesByUserId(Long id) {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ROLES_BY_USER_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    roles.add(new RoleCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get roles by user id", e);
            throw new DaoException("Can't get roles by user id", e);
        }
        return roles;
    }

    @Override
    public List<Role> getRolesByUserLogin(String login) {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ROLES_BY_USER_LOGIN)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    roles.add(new RoleCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get roles by user login", e);
            throw new DaoException("Can't get roles by user login", e);
        }
        return roles;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_ROLES)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    roles.add(new RoleCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get all roles", e);
            throw new DaoException("Can't get all roles", e);
        }
        return roles;
    }
}
