package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.enums.constants.UserQueries;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {
    private final UserQueries userQueries;
    private final Collector<User> userCollector;

    @Autowired
    public UserDaoImpl(UserQueries userQueries,
                       @Qualifier("userCollector") Collector<User> userCollector) {
        this.userQueries = userQueries;
        this.userCollector = userCollector;
    }

    @Override
    public User create(Connection connection, User model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> create(Connection connection, List<User> models) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<User> getById(Connection connection, long id) {
        log.info("Getting user by id: {}", id);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryById())) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting user by id: {}", id, e);
            throw new DaoException("Error getting user by id: " + id, e);
        }
    }

    @Override
    public List<User> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting users by ids: {}", ids);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryByIds(ids.size()))) {
            statement.setFetchSize(ids.size());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }
            try (var resultSet = statement.executeQuery()) {
                return userCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting users by ids: {}", ids, e);
            throw new DaoException("Error getting users by ids: " + ids, e);
        }
    }

    @Override
    public List<User> getAll(Connection connection) {
        log.info("Getting all users");
        try (var statement = connection.prepareStatement(
                userQueries.getQueryAll())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (var resultSet = statement.executeQuery()) {
                return userCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all users", e);
            throw new DaoException("Error getting all users", e);
        }
    }

    @Override
    public List<User> getAll(Connection connection, Sort sort) {
        log.info("Getting all users with sort: {}", sort);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryAll(sort))) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (var resultSet = statement.executeQuery()) {
                return userCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all users with sort: {}", sort, e);
            throw new DaoException("Error getting all users with sort: " + sort, e);
        }
    }

    @Override
    public Page<User> getPage(Connection connection, Pageable page) {
        log.info("Getting page of users with page: {}", page);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryPage(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (var resultSet = statement.executeQuery()) {
                List<User> users = userCollector.collectList(resultSet);
                resultSet.last();
                if (resultSet.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(users, page, resultSet.getLong(11));
            }
        } catch (SQLException e) {
            log.error("Error getting page of users with page: {}", page, e);
            throw new DaoException("Error getting page of users with page: " + page, e);
        }
    }

    @Override
    public void update(Connection connection, User entity) {
        log.info("Updating user: {}", entity);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryUpdate())) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPhone());
            statement.setBoolean(6, entity.isEnabled());
            statement.setLong(7, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating user: {}", entity, e);
            throw new DaoException("Error updating user: " + entity, e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting user by id: {}", id);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryDelete())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting user by id: {}", id, e);
            throw new DaoException("Error deleting user by id: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting users");
        try (var statement = conn.prepareStatement(
                userQueries.getQueryCount())) {
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting users", e);
            throw new DaoException("Error counting users", e);
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all users");
        try (var statement = conn.prepareStatement(
                userQueries.getQueryDeleteAll())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all users", e);
            throw new DaoException("Error deleting all users", e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting users by ids: {}", longs);
        try (var statement = conn.prepareStatement(
                userQueries.getQueryDeleteByIds(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                statement.setLong(i + 1, longs.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting users by ids: {}", longs, e);
            throw new DaoException("Error deleting users by ids: " + longs, e);
        }
    }

    @Override
    public void update(Connection conn, List<User> entities) {
        log.info("Updating users: {}", entities);
        try (var statement = conn.prepareStatement(
                userQueries.getQueryUpdate())) {
            for (User entity : entities) {
                statement.setString(1, entity.getLogin());
                statement.setString(2, entity.getFirstName());
                statement.setString(3, entity.getLastName());
                statement.setString(4, entity.getEmail());
                statement.setString(5, entity.getPhone());
                statement.setBoolean(6, entity.isEnabled());
                statement.setLong(7, entity.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating users: {}", entities, e);
            throw new DaoException("Error updating users: " + entities, e);
        }
    }

    @Override
    public User create(Connection connection, User entity, String password) {
        log.info("Creating user: {}", entity);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryCreate(), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPhone());
            statement.setBoolean(6, entity.isEnabled());
            statement.setString(7, password);
            statement.executeUpdate();
            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                }
            }
            throw new DaoException("Error creating user: " + entity);
        } catch (SQLException e) {
            log.error("Error creating user: {}", entity, e);
            throw new DaoException("Error creating user: " + entity, e);
        }
    }

    @Override
    public void update(Connection connection, User entity, String password) {
        log.info("Updating user: {}", entity);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryUpdatePassword())) {
            statement.setString(1, password);
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating user: {}", entity, e);
            throw new DaoException("Error updating user: " + entity, e);
        }
    }

    @Override
    public Optional<User> getByLogin(Connection connection, String login) {
        log.info("Getting user by login: {}", login);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryGetByLogin())) {
            statement.setString(1, login);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting user by login: {}", login, e);
            throw new DaoException("Error getting user by login: " + login, e);
        }
    }

    @Override
    public Optional<User> getByEmail(Connection connection, String email) {
        log.info("Getting user by email: {}", email);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryGetByEmail())) {
            statement.setString(1, email);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting user by email: {}", email, e);
            throw new DaoException("Error getting user by email: " + email, e);
        }
    }

    @Override
    public Optional<User> getByPhone(Connection connection, String phone) {
        log.info("Getting user by phone: {}", phone);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryGetByPhone())) {
            statement.setString(1, phone);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting user by phone: {}", phone, e);
            throw new DaoException("Error getting user by phone: " + phone, e);
        }
    }

    @Override
    public Optional<User> getByOrderId(Connection connection, Long id) {
        log.info("Getting user by order id: {}", id);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryGetByOrderId())) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting user by order id: {}", id, e);
            throw new DaoException("Error getting user by order id: " + id, e);
        }
    }

    @Override
    public Optional<User> getByHistoryOrderId(Connection connection, Long id) {
        log.info("Getting user by history order id: {}", id);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryGetByHistoryOrderId())) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting user by history order id: {}", id, e);
            throw new DaoException("Error getting user by history order id: " + id, e);
        }
    }

    @Override
    public void disable(Connection connection, Long id) {
        log.info("Disabling user by id: {}", id);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryDisable())) {
            statement.setBoolean(1, false);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error disabling user by id: {}", id, e);
            throw new DaoException("Error disabling user by id: " + id, e);
        }
    }

    @Override
    public void enable(Connection connection, Long id) {
        log.info("Enabling user by id: {}", id);
        try (var statement = connection.prepareStatement(
                userQueries.getQueryEnable())) {
            statement.setBoolean(1, true);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error enabling user by id: {}", id, e);
            throw new DaoException("Error enabling user by id: " + id, e);
        }
    }

    @Override
    public void updateRolesByUserId(Connection connection, Long id, Collection<Role> roles) {
        log.info("Updating roles for user by id: {}", id);
        deleteRolesRelation(connection, id);
        insertRolesRelation(connection, id, roles);
    }

    @Override
    public Page<User> searchUsers(Connection connection, Pageable pageable, String search) {
        log.info("Searching users by search: {}", search);
        try (PreparedStatement statement = connection.prepareStatement(
                userQueries.getQuerySearchUsers(pageable, search),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(pageable.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, "%" + search + "%");
            statement.setString(4, "%" + search + "%");
            statement.setString(5, "%" + search + "%");
            statement.setLong(6, pageable.getPageSize());
            statement.setLong(7, pageable.getOffset());
            try (var resultSet = statement.executeQuery()) {
                List<User> users = userCollector.collectList(resultSet);
                resultSet.last();
                if (resultSet.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), pageable, 0);
                }
                return new PageImpl<>(users, pageable, resultSet.getLong(11));
            }
        } catch (SQLException e) {
            log.error("Error searching users by search: {}", search, e);
            throw new DaoException("Error searching users by search: " + search, e);
        }
    }

    private void insertRolesRelation(Connection connection, Long id, Collection<Role> roles) {
        try (var statement = connection.prepareStatement(
                userQueries.getQueryInsertRolesByUserId())) {
            for (Role role : roles) {
                statement.setLong(1, id);
                statement.setLong(2, role.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error("Error inserting roles for user by id: {}", id, e);
            throw new DaoException("Error inserting roles for user by id: " + id, e);
        }
    }

    private void deleteRolesRelation(Connection connection, Long id) {
        try (var statement = connection.prepareStatement(
                userQueries.getQueryDeleteRolesByUserId())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting roles for user by id: {}", id, e);
            throw new DaoException("Error deleting roles for user by id: " + id, e);
        }
    }
}
