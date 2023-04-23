package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.postgres_queries.StatusNameQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.StatusNameDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.StatusName;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
public class StatusNameDaoImpl implements StatusNameDao {
    private final StatusNameQueries statusNameQueries;
    private final Collector<StatusName> statusNameCollector;

    @Autowired
    public StatusNameDaoImpl(StatusNameQueries statusNameQueries,
                             @Qualifier("statusNameCollector") Collector<StatusName> statusNameCollector) {
        this.statusNameQueries = statusNameQueries;
        this.statusNameCollector = statusNameCollector;
    }

    @Override
    public StatusName create(Connection connection, StatusName model) {
        log.info("Creating status name: {}", model);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getCreateStatusNameQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, model.getName());
            preparedStatement.setString(2, model.getLang());
            preparedStatement.setLong(3, model.getStatus().getId());
            preparedStatement.executeUpdate();
            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getLong(1));
                }
            }
            return model;
        } catch (SQLException e) {
            log.error("Error creating status name: {}", e.getMessage());
            throw new DaoException("Error creating status name: " + e.getMessage());
        }
    }

    @Override
    public List<StatusName> create(Connection connection, List<StatusName> models) {
        log.info("Creating status names: {}", models);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getCreateStatusNameQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            for (StatusName model : models) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setString(2, model.getLang());
                preparedStatement.setLong(3, model.getStatus().getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                for (StatusName model : models) {
                    if (generatedKeys.next()) {
                        model.setId(generatedKeys.getLong(1));
                    }
                }
            }
            return models;
        } catch (SQLException e) {
            log.error("Error creating status names: {}", e.getMessage());
            throw new DaoException("Error creating status names: " + e.getMessage());
        }
    }

    @Override
    public Optional<StatusName> getById(Connection connection, long id) {
        log.info("Getting status name by id: {}", id);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getGetStatusNameByIdQuery())) {
            preparedStatement.setLong(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(statusNameCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting status name by id: {}", e.getMessage());
            throw new DaoException("Error getting status name by id: " + e.getMessage());
        }
    }

    @Override
    public List<StatusName> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting status names by ids: {}", ids);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getGetStatusNameByIdsQuery(ids.size()))) {
            preparedStatement.setFetchSize(ids.size());
            preparedStatement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (Long id : ids) {
                preparedStatement.setLong(1, id);
                preparedStatement.addBatch();
            }
            try (var resultSet = preparedStatement.executeQuery()) {
                return statusNameCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting status names by ids: {}", e.getMessage());
            throw new DaoException("Error getting status names by ids: " + e.getMessage());
        }
    }

    @Override
    public List<StatusName> getAll(Connection connection) {
        log.info("Getting all status names");
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getGetAllStatusNamesQuery())) {
            preparedStatement.setFetchSize(100);
            preparedStatement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return statusNameCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all status names: {}", e.getMessage());
            throw new DaoException("Error getting all status names: " + e.getMessage());
        }
    }

    @Override
    public List<StatusName> getAll(Connection connection, Sort sort) {
        log.info("Getting all status names with sort: {}", sort);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getGetAllStatusNamesQuery(sort))) {
            preparedStatement.setFetchSize(100);
            preparedStatement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return statusNameCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all status names with sort: {}", e.getMessage());
            throw new DaoException("Error getting all status names with sort: " + e.getMessage());
        }
    }

    @Override
    public Page<StatusName> getPage(Connection connection, Pageable page) {
        log.info("Getting page of status names: {}", page);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getGetPageStatusNamesQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            preparedStatement.setFetchSize(page.getPageSize());
            preparedStatement.setFetchDirection(ResultSet.FETCH_FORWARD);
            preparedStatement.setInt(1, page.getPageSize());
            preparedStatement.setLong(2, page.getOffset());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<StatusName> statusNames = statusNameCollector.collectList(resultSet);
                resultSet.last();
                if (resultSet.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(statusNames, page, resultSet.getLong(5));
            }
        } catch (SQLException e) {
            log.error("Error getting page of status names: {}", e.getMessage());
            throw new DaoException("Error getting page of status names: " + e.getMessage());
        }
    }

    @Override
    public void update(Connection connection, StatusName entity) {
        log.info("Updating status name: {}", entity);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getUpdateStatusNameQuery())) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLang());
            preparedStatement.setLong(3, entity.getStatus().getId());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating status name: {}", e.getMessage());
            throw new DaoException("Error updating status name: " + e.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting status name by id: {}", id);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                statusNameQueries.getDeleteStatusNameByIdQuery())) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting status name by id: {}", e.getMessage());
            throw new DaoException("Error deleting status name by id: " + e.getMessage());
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting status names");
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                statusNameQueries.getGetCountStatusNamesQuery())) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting status names: {}", e.getMessage());
            throw new DaoException("Error counting status names: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all status names");
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                statusNameQueries.getDeleteAllStatusNamesQuery())) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all status names: {}", e.getMessage());
            throw new DaoException("Error deleting all status names: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting status names by ids: {}", longs);
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                statusNameQueries.getDeleteStatusNamesByIdsQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                preparedStatement.setLong(i + 1, longs.get(i));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting status names by ids: {}", e.getMessage());
            throw new DaoException("Error deleting status names by ids: " + e.getMessage());
        }
    }

    @Override
    public void update(Connection conn, List<StatusName> entities) {
        log.info("Updating status names: {}", entities);
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                statusNameQueries.getUpdateStatusNamesQuery())) {
            for (StatusName entity : entities) {
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getLang());
                preparedStatement.setLong(3, entity.getStatus().getId());
                preparedStatement.setLong(4, entity.getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating status names: {}", e.getMessage());
            throw new DaoException("Error updating status names: " + e.getMessage());
        }
    }

    @Override
    public List<StatusName> getByStatusId(Connection conn, Long id) {
        log.info("Getting status names by status id: {}", id);
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                statusNameQueries.getGetStatusNamesByStatusIdQuery())) {
            preparedStatement.setFetchSize(100);
            preparedStatement.setFetchDirection(ResultSet.FETCH_FORWARD);
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return statusNameCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting status names by status id: {}", e.getMessage());
            throw new DaoException("Error getting status names by status id: " + e.getMessage());
        }
    }

    @Override
    public Optional<StatusName> getByStatusId(Connection conn, Long statusId, Locale locale) {
        log.info("Getting status name by status id: {} and locale: {}", statusId, locale);
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                statusNameQueries.getGetStatusNameByStatusIdAndLocaleQuery())) {
            preparedStatement.setLong(1, statusId);
            preparedStatement.setString(2, locale.getLanguage());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(statusNameCollector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting status name by status id: {} and locale: {}", e.getMessage());
            throw new DaoException("Error getting status name by status id: " + e.getMessage());
        }
    }

}
