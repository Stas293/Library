package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.dao.collector.UserCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCUserDao implements UserDao {
    //language=MySQL
    private static final String INSERT_USER_ROLE = "CALL INSERT_USER_ROLE (?, ?);";
    //language=MySQL
    private static final String INSERT_USER = "CALL INSERT_USER_LIST (?,?,?,?,?,?,?, ?);";
    //language=MySQL
    private static final String DELETE_ROLES_BY_USER_ID = "delete from user_role where user_id = ?;";
    //language=MySQL
    private static final String CHANGE_PASSWORD = "update user_list set `password` = ? where id = ?;";
    //language=MySQL
    private static final String GET_BY_ID = "select * from user_list where id = ?;";
    //language=MySQL
    private static final String GET_USER_PAGE = "CALL GET_USER_PAGE (?, ?, ?, ?);";
    //language=MySQL
    private static final String DELETE_USER_BY_ID = "delete from user_list where id = ?;";
    //language=MySQL
    private static final String GET_USER_BY_LOGIN = "select * from user_list where login = ?;";
    //language=MySQL
    private static final String GET_USER_BY_EMAIL = "select * from user_list where email = ?;";
    //language=MySQL
    private static final String GET_USER_BY_PHONE = "select * from user_list where phone = ?;";
    //language=MySQL
    private static final String GET_USER_BY_ORDER_ID =
            "select u.* from user_list u inner join " +
                    "order_list ol on u.id = ol.user_id " +
                    "where ol.id = ?;";
    //language=MySQL
    private static final String GET_USER_BY_HISTORY_ORDER_ID =
            "select u.* from user_list u inner join " +
                    "history_order oh on u.id = oh.user_id " +
                    "where oh.id = ?;";
    //language=MySQL
    private static final String DISABLE_USER_BY_ID = "update user_list set enabled = 0 where id = ?;";
    //language=MySQL
    private static final String DISABLE_USER_BY_LOGIN = "update user_list set enabled = 0 where login = ?;";
    //language=MySQL
    private static final String ENABLE_USER_BY_ID = "update user_list set enabled = 1 where id = ?;";
    //language=MySQL
    private static final String ENABLE_USER_BY_LOGIN = "update user_list set enabled = 1 where login = ?;";
    //language=MySQL
    private static final String UPDATE_EMAIL = "update user_list set email = ? where id = ?;";
    //language=MySQL
    private static final String UPDATE_PHONE = "update user_list set phone = ? where id = ?;";
    //language=MySQL
    private static final String UPDATE_PASSWORD = "update user_list set password = ? where id = ?;";
    //language=MySQL
    private static final String UPDATE_FIRST_NAME = "update user_list set first_name = ? where id = ?;";
    //language=MySQL
    private static final String UPDATE_LAST_NAME = "update user_list set last_name = ? where id = ?;";
    private static final Logger LOGGER = LogManager.getLogger(JDBCUserDao.class);
    private final Connection connection;

    public JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long create(User model) throws SQLException {
        return Constants.APP_DEFAULT_ID;
    }

    @Override
    public Optional<User> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new UserCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error while getting user by id", e);
            throw new DaoException(e);
        }
        return Optional.empty();
    }

    @Override
    public Page<User> getPage(Page<User> page) {
        List<User> users = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall(GET_USER_PAGE)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, page.getSearch());
            statement.setString(4, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                UserCollector userCollector = new UserCollector();
                while (resultSet.next()) {
                    users.add(userCollector.collectFromResultSet(resultSet));
                }
            }
            page.setData(users);
            page.setElementsCount(users.size());
        } catch (SQLException e) {
            LOGGER.error("Error while getting user page", e);
            throw new DaoException(e);
        }
        return page;
    }

    @Override
    public void update(User entity) throws SQLException {
        connection.setAutoCommit(false);
        try {
            deleteRolesForUser(entity.getId());
            insertRolesForUser(entity);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while updating user", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void insertRolesForUser(User entity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE)) {
            for (Role role : entity.getRoles()) {
                statement.setLong(1, entity.getId());
                statement.setLong(2, role.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Can't insert roles for user", e);
            throw e;
        }
    }

    private void deleteRolesForUser(Long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ROLES_BY_USER_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(String.format("Can't delete roles for user with id: %d", id), e);
            throw e;
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            deleteRolesForUser(id);
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error(String.format("Can't delete user with id: %d", id), e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Error while closing connection", e);
            throw new DaoException(e);
        }
    }

    @Override
    public long create(User entity, String password) throws SQLException {
        connection.setAutoCommit(false);
        try {
            long gotId = insertUser(entity, password);
            setRolesForUser(gotId, entity);
            connection.commit();
            return gotId;
        } catch (SQLException e) {
            LOGGER.error("Error while creating user", e);
            connection.rollback();
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void setRolesForUser(long gotId, User entity) throws SQLException {
        for (Role role : entity.getRoles()) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE)) {
                statement.setLong(1, gotId);
                statement.setLong(2, role.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("Can't set role for user", e);
                throw e;
            }
        }
    }

    private long insertUser(User entity, String password) throws SQLException {
        try (CallableStatement statement = connection.prepareCall(INSERT_USER)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPhone());
            statement.setString(6, password);
            statement.setBoolean(7, entity.isEnabled());
            statement.registerOutParameter(8, Types.BIGINT);
            statement.executeUpdate();
            return statement.getLong(8);
        } catch (SQLException e) {
            LOGGER.error("Can't insert user", e);
            throw e;
        }
    }

    @Override
    public void update(User entity, String password) throws SQLException {
        connection.setAutoCommit(false);
        try {
            updateUser(entity, password);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while updating user", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void updateUser(User entity, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CHANGE_PASSWORD)) {
            statement.setString(1, password);
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Can't update user", e);
            throw e;
        }
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return getUserByUniqueString(login, GET_USER_BY_LOGIN);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return getUserByUniqueString(email, GET_USER_BY_EMAIL);
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        return getUserByUniqueString(phone, GET_USER_BY_PHONE);
    }

    @Override
    public Optional<User> getByOrderId(Long id) {
        return getUserByUniqueLong(id, GET_USER_BY_ORDER_ID);
    }

    @Override
    public Optional<User> getByHistoryOrderId(Long id) {
        return getUserByUniqueLong(id, GET_USER_BY_HISTORY_ORDER_ID);
    }

    @Override
    public void disable(Long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DISABLE_USER_BY_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while disabling user", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void disable(String login) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DISABLE_USER_BY_LOGIN)) {
            statement.setString(1, login);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while disabling user", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void enable(Long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(ENABLE_USER_BY_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while enabling user", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void enable(String login) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(ENABLE_USER_BY_LOGIN)) {
            statement.setString(1, login);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while enabling user", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void updateEmail(String email, User user) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_EMAIL)) {
            statement.setString(1, email);
            statement.setLong(2, user.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while updating email", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void updatePhone(String phone, User user) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PHONE)) {
            statement.setString(1, phone);
            statement.setLong(2, user.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while updating phone", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void updateFirstName(String firstName, User user) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_FIRST_NAME)) {
            statement.setString(1, firstName);
            statement.setLong(2, user.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while updating first name", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void updateLastName(String lastName, User user) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_LAST_NAME)) {
            statement.setString(1, lastName);
            statement.setLong(2, user.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Error while updating last name", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private Optional<User> getUserByUniqueString(String field, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, field);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new UserCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get user by unique field", e);
            throw new DaoException(e);
        }
        return Optional.empty();
    }

    private Optional<User> getUserByUniqueLong(Long field, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, field);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new UserCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get user by unique field", e);
            throw new DaoException(e);
        }
        return Optional.empty();
    }
}
