package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.enums.constants.RoleQueries;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Role;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class RoleDaoImpl implements RoleDao {
    private final RoleQueries roleQueries;
    private final Collector<Role> roleCollector;

    @Autowired
    public RoleDaoImpl(RoleQueries roleQueries,
                       @Qualifier("roleCollector") Collector<Role> roleCollector) {
        this.roleQueries = roleQueries;
        this.roleCollector = roleCollector;
    }

    @Override
    public Role create(Connection connection, Role model) {
        log.info("Creating role: {}", model);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getCode());
            statement.setString(2, model.getName());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getLong(1));
                    return model;
                } else {
                    throw new SQLException("Creating role failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            log.error("Error creating role: {}", e.getMessage());
            throw new DaoException("Error creating role: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Role> create(Connection connection, List<Role> models) {
        log.info("Creating roles: {}", models);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            for (Role model : models) {
                statement.setString(1, model.getCode());
                statement.setString(2, model.getName());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                for (Role model : models) {
                    if (generatedKeys.next()) {
                        model.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating role failed, no ID obtained.");
                    }
                }
            }
            return models;
        } catch (SQLException e) {
            log.error("Error creating roles: {}", e.getMessage());
            throw new DaoException("Error creating roles: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Role> getById(Connection connection, long id) {
        log.info("Getting role by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetByIdQuery())) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(roleCollector.collect(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Error getting role by id: {}", e.getMessage());
            throw new DaoException("Error getting role by id: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Role> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting roles by ids: {}", ids);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetByIdsQuery(ids.size()))) {
            statement.setFetchSize(ids.size());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }
            try (ResultSet rs = statement.executeQuery()) {
                return roleCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting roles by ids: {}", e.getMessage());
            throw new DaoException("Error getting roles by ids: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Role> getAll(Connection connection) {
        log.info("Getting all roles");
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetAllQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return roleCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all roles: {}", e.getMessage());
            throw new DaoException("Error getting all roles: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Role> getAll(Connection connection, Sort sort) {
        log.info("Getting all roles with sort: {}", sort);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetAllQuery(sort))) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return roleCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all roles with sort: {}", e.getMessage());
            throw new DaoException("Error getting all roles with sort: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Role> getPage(Connection connection, Pageable page) {
        log.info("Getting page of roles: {}", page);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetPageQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (ResultSet rs = statement.executeQuery()) {
                List<Role> roles = roleCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(roles, page, rs.getLong(4));
            }
        } catch (SQLException e) {
            log.error("Error getting page of roles: {}", e.getMessage());
            throw new DaoException("Error getting page of roles: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Connection connection, Role entity) {
        log.info("Updating role: {}", entity);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getUpdateQuery())) {
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getName());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating role: {}", e.getMessage());
            throw new DaoException("Error updating role: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting role by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getDeleteQuery())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting role by id: {}", e.getMessage());
            throw new DaoException("Error deleting role by id: " + e.getMessage(), e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting roles");
        try (PreparedStatement statement = conn.prepareStatement(
                roleQueries.getGetCountQuery())) {
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting roles: {}", e.getMessage());
            throw new DaoException("Error counting roles: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all roles");
        try (PreparedStatement statement = conn.prepareStatement(
                roleQueries.getDeleteAllQuery())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all roles: {}", e.getMessage());
            throw new DaoException("Error deleting all roles: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting roles by ids: {}", longs);
        try (PreparedStatement statement = conn.prepareStatement(
                roleQueries.getDeleteAllQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                statement.setLong(i + 1, longs.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting roles by ids: {}", e.getMessage());
            throw new DaoException("Error deleting roles by ids: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Connection conn, List<Role> entities) {
        log.info("Updating roles: {}", entities);
        try (PreparedStatement statement = conn.prepareStatement(
                roleQueries.getUpdateQuery())) {
            for (Role role : entities) {
                statement.setString(1, role.getCode());
                statement.setString(2, role.getName());
                statement.setLong(3, role.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating roles: {}", e.getMessage());
            throw new DaoException("Error updating roles: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Role> getByCode(Connection connection, String code) {
        log.info("Getting role by code: {}", code);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetByCodeQuery())) {
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(roleCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting role by code: {}", e.getMessage());
            throw new DaoException("Error getting role by code: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Role> getByName(Connection connection, String name) {
        log.info("Getting role by name: {}", name);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetByNameQuery())) {
            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(roleCollector.collect(rs));
            }
        } catch (SQLException e) {
            log.error("Error getting role by name: {}", e.getMessage());
            throw new DaoException("Error getting role by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Role> getRolesByUserId(Connection connection, Long id) {
        log.info("Getting roles by user id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetRolesByUserIdQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return roleCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting roles by user id: {}", e.getMessage());
            throw new DaoException("Error getting roles by user id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Role> getAllByCodes(Connection connection, List<String> roles) {
        log.info("Getting roles by codes: {}", roles);
        try (PreparedStatement statement = connection.prepareStatement(
                roleQueries.getGetAllByCodesQuery(roles.size()))) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < roles.size(); i++) {
                statement.setString(i + 1, roles.get(i));
            }
            try (ResultSet rs = statement.executeQuery()) {
                return roleCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting roles by codes: {}", e.getMessage());
            throw new DaoException("Error getting roles by codes: " + e.getMessage(), e);
        }
    }
}
