package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.postgres_queries.StatusQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Status;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class StatusDaoImpl implements StatusDao {
    private final StatusQueries statusQueries;
    private final Collector<Status> statusCollector;

    @Autowired
    public StatusDaoImpl(StatusQueries statusQueries,
                         @Qualifier("statusCollector") Collector<Status> statusCollector) {
        this.statusQueries = statusQueries;
        this.statusCollector = statusCollector;
    }

    @Override
    public Status create(Connection connection, Status model) {
        log.info("Creating status: {}", model);
        try (var statement = connection.prepareStatement(
                statusQueries.getCreateStatusQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getCode());
            statement.setBoolean(2, model.getClosed());
            statement.executeUpdate();
            try (var resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    model.setId(resultSet.getLong(1));
                    return model;
                } else {
                    log.error("Error creating status: {}", model);
                    throw new DaoException("Error creating status: " + model);
                }
            }
        } catch (SQLException e) {
            log.error("Error creating status: {}", e.getMessage());
            throw new DaoException("Error creating status: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Status> create(Connection connection, List<Status> models) {
        log.info("Creating statuses: {}", models);
        try (var statement = connection.prepareStatement(
                statusQueries.getCreateStatusQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            for (Status model : models) {
                statement.setString(1, model.getCode());
                statement.setBoolean(2, model.getClosed());
                statement.addBatch();
            }
            statement.executeBatch();
            try (var resultSet = statement.getGeneratedKeys()) {
                return statusCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error creating statuses: {}", e.getMessage());
            throw new DaoException("Error creating statuses: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Status> getById(Connection connection, long id) {
        log.info("Getting status by id: {}", id);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetStatusByIdQuery())) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                return Optional.of(statusCollector.collect(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error getting status by id: {}", e.getMessage());
            throw new DaoException("Error getting status by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Status> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting statuses by ids: {}", ids);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetStatusesByIdsQuery(ids.size()))) {
            for (Long id : ids) {
                statement.setLong(1, id);
                statement.addBatch();
            }
            try (var resultSet = statement.executeQuery()) {
                return statusCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting statuses by ids: {}", e.getMessage());
            throw new DaoException("Error getting statuses by ids: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Status> getAll(Connection connection) {
        log.info("Getting all statuses");
        try (var statement = connection.prepareStatement(
                statusQueries.getGetAllStatusesQuery())) {
            try (var resultSet = statement.executeQuery()) {
                return statusCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all statuses: {}", e.getMessage());
            throw new DaoException("Error getting all statuses: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Status> getAll(Connection connection, Sort sort) {
        log.info("Getting all statuses with sort: {}", sort);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetAllStatusesQuery(sort))) {
            try (var resultSet = statement.executeQuery()) {
                return statusCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all statuses with sort: {}", e.getMessage());
            throw new DaoException("Error getting all statuses with sort: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Status> getPage(Connection connection, Pageable page) {
        log.info("Getting page of statuses: {}", page);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetPageStatusesQuery(page))) {
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (var resultSet = statement.executeQuery()) {
                return new PageImpl<>(statusCollector.collectList(resultSet), page, count(connection));
            }
        } catch (SQLException e) {
            log.error("Error getting page of statuses: {}", e.getMessage());
            throw new DaoException("Error getting page of statuses: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Connection connection, Status entity) {
        log.info("Updating status: {}", entity);
        try (var statement = connection.prepareStatement(
                statusQueries.getUpdateStatusQuery())) {
            statement.setString(1, entity.getCode());
            statement.setBoolean(2, entity.getClosed());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating status: {}", e.getMessage());
            throw new DaoException("Error updating status: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting status by id: {}", id);
        try (var statement = connection.prepareStatement(
                statusQueries.getDeleteStatusByIdQuery())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting status by id: {}", e.getMessage());
            throw new DaoException("Error deleting status by id: " + e.getMessage(), e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting statuses");
        try (var statement = conn.prepareStatement(
                statusQueries.getGetCountStatusesQuery())) {
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong(1) : 0;
            }
        } catch (SQLException e) {
            log.error("Error counting statuses: {}", e.getMessage());
            throw new DaoException("Error counting statuses: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all statuses");
        try (var statement = conn.prepareStatement(
                statusQueries.getDeleteAllStatusesQuery())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all statuses: {}", e.getMessage());
            throw new DaoException("Error deleting all statuses: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting statuses by ids: {}", longs);
        try (var statement = conn.prepareStatement(
                statusQueries.getDeleteStatusesByIdsQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                statement.setLong(i + 1, longs.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting statuses by ids: {}", e.getMessage());
            throw new DaoException("Error deleting statuses by ids: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Connection conn, List<Status> entities) {
        log.info("Updating statuses: {}", entities);
        try (var statement = conn.prepareStatement(
                statusQueries.getUpdateStatusQuery())) {
            for (Status status : entities) {
                statement.setString(1, status.getCode());
                statement.setBoolean(2, status.getClosed());
                statement.setLong(3, status.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating statuses: {}", e.getMessage());
            throw new DaoException("Error updating statuses: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Status> getByCode(Connection connection, String code) {
        log.info("Getting status by code: {}", code);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetStatusByCodeQuery())) {
            statement.setString(1, code);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(statusCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting status by code: {}", e.getMessage());
            throw new DaoException("Error getting status by code: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Status> getByOrderId(Connection connection, long orderId) {
        log.info("Getting status by order id: {}", orderId);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetStatusByOrderIdQuery())) {
            statement.setLong(1, orderId);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(statusCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting status by order id: {}", e.getMessage());
            throw new DaoException("Error getting status by order id: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Status> getByHistoryOrderId(Connection connection, long historyId) {
        log.info("Getting status by history order id: {}", historyId);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetStatusByHistoryOrderIdQuery())) {
            statement.setLong(1, historyId);
            try (var resultSet = statement.executeQuery()) {
                return Optional.ofNullable(statusCollector.collect(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error getting status by history order id: {}", e.getMessage());
            throw new DaoException("Error getting status by history order id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Status> getNextStatusesForStatusById(Connection connection, Long id) {
        log.info("Getting next statuses for status by id: {}", id);
        try (var statement = connection.prepareStatement(
                statusQueries.getGetNextStatusesForStatusByIdQuery())) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                return statusCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting next statuses for status by id: {}", e.getMessage());
            throw new DaoException("Error getting next statuses for status by id: " + e.getMessage(), e);
        }
    }
}
