package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.postgres_queries.HistoryOrderQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.HistoryOrderDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.HistoryOrder;
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
public class HistoryOrderDaoImpl implements HistoryOrderDao {
    private final HistoryOrderQueries historyOrderQueries;
    private final Collector<HistoryOrder> historyOrderCollector;

    @Autowired
    public HistoryOrderDaoImpl(HistoryOrderQueries historyOrderQueries,
                               @Qualifier("historyOrderCollector") Collector<HistoryOrder> historyOrderCollector) {
        this.historyOrderQueries = historyOrderQueries;
        this.historyOrderCollector = historyOrderCollector;
    }

    @Override
    public HistoryOrder create(Connection connection, HistoryOrder model) {
        log.info("Creating history order: {}", model);
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getBookTitle());
            statement.setObject(2, model.getDateCreated());
            statement.setObject(3, model.getDateReturned());
            statement.setLong(4, model.getUser().getId());
            statement.setLong(5, model.getStatus().getId());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getLong(1));
                    return model;
                }
                throw new SQLException("Creating history order failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error creating history order: {}", e.getMessage());
            throw new DaoException("Error creating history order: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoryOrder> create(Connection connection, List<HistoryOrder> models) {
        log.info("Creating history orders: {}", models);
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getCreateQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            for (HistoryOrder model : models) {
                statement.setString(1, model.getBookTitle());
                statement.setObject(2, model.getDateCreated());
                statement.setObject(3, model.getDateReturned());
                statement.setLong(4, model.getUser().getId());
                statement.setLong(5, model.getStatus().getId());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                for (HistoryOrder model : models) {
                    if (generatedKeys.next()) {
                        model.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating history order failed, no ID obtained.");
                    }
                }
            }
            return models;
        } catch (SQLException e) {
            log.error("Error creating history orders: {}", e.getMessage());
            throw new DaoException("Error creating history orders: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<HistoryOrder> getById(Connection connection, long id) {
        log.info("Getting history order by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getSelectByIdQuery())) {
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(historyOrderCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting history order by id: {}", e.getMessage());
            throw new DaoException("Error getting history order by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoryOrder> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting history orders by ids: {}", ids);
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getSelectByIdsQuery(ids.size()))) {
            statement.setFetchSize(ids.size());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }
            try (ResultSet rs = statement.executeQuery()) {
                return historyOrderCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting history orders by ids: {}", e.getMessage());
            throw new DaoException("Error getting history orders by ids: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoryOrder> getAll(Connection connection) {
        log.info("Getting all history orders");
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getSelectAllQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return historyOrderCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all history orders: {}", e.getMessage());
            throw new DaoException("Error getting all history orders: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoryOrder> getAll(Connection connection, Sort sort) {
        log.info("Getting all history orders");
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getSelectAllQuery(sort))) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return historyOrderCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all history orders: {}", e.getMessage());
            throw new DaoException("Error getting all history orders: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<HistoryOrder> getPage(Connection connection, Pageable page) {
        log.info("Getting page of history orders");
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getSelectAllQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(page.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (ResultSet rs = statement.executeQuery()) {
                List<HistoryOrder> historyOrders = historyOrderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(historyOrders, page, rs.getRow());
            }
        } catch (SQLException e) {
            log.error("Error getting page of history orders: {}", e.getMessage());
            throw new DaoException("Error getting page of history orders: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Connection connection, HistoryOrder entity) {
        log.info("Updating history order: {}", entity);
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getUpdateQuery())) {
            statement.setString(1, entity.getBookTitle());
            statement.setObject(2, entity.getDateCreated());
            statement.setObject(3, entity.getDateReturned());
            statement.setLong(4, entity.getUser().getId());
            statement.setLong(5, entity.getStatus().getId());
            statement.setLong(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating history order: {}", e.getMessage());
            throw new DaoException("Error updating history order: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting history order by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                historyOrderQueries.getDeleteQuery())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting history order by id: {}", e.getMessage());
            throw new DaoException("Error deleting history order by id: " + e.getMessage(), e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting history orders");
        try (PreparedStatement statement = conn.prepareStatement(
                historyOrderQueries.getCountQuery())) {
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting history orders: {}", e.getMessage());
            throw new DaoException("Error counting history orders: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all history orders");
        try (PreparedStatement statement = conn.prepareStatement(
                historyOrderQueries.getDeleteAllQuery())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all history orders: {}", e.getMessage());
            throw new DaoException("Error deleting all history orders: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting history orders by ids: {}", longs);
        try (PreparedStatement statement = conn.prepareStatement(
                historyOrderQueries.getDeleteAllByIdsQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                statement.setLong(i + 1, longs.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting history orders by ids: {}", e.getMessage());
            throw new DaoException("Error deleting history orders by ids: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Connection conn, List<HistoryOrder> entities) {
        log.info("Updating history orders: {}", entities);
        try (PreparedStatement statement = conn.prepareStatement(
                historyOrderQueries.getUpdateQuery())) {
            for (HistoryOrder entity : entities) {
                statement.setString(1, entity.getBookTitle());
                statement.setObject(2, entity.getDateCreated());
                statement.setObject(3, entity.getDateReturned());
                statement.setLong(4, entity.getUser().getId());
                statement.setLong(5, entity.getStatus().getId());
                statement.setLong(6, entity.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating history orders: {}", e.getMessage());
            throw new DaoException("Error updating history orders: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<HistoryOrder> getPageByUserId(Connection conn, Pageable page, Long userId) {
        log.info("Getting page of history orders by user id: {}", userId);
        try (PreparedStatement statement = conn.prepareStatement(
                historyOrderQueries.getSelectAllByUserIdQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(page.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, userId);
            statement.setInt(2, page.getPageSize());
            statement.setLong(3, page.getOffset());
            try (ResultSet rs = statement.executeQuery()) {
                List<HistoryOrder> historyOrders = historyOrderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(historyOrders, page, rs.getLong(7));
            }
        } catch (SQLException e) {
            log.error("Error getting page of history orders by user id: {}", e.getMessage());
            throw new DaoException("Error getting page of history orders by user id: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<HistoryOrder> getPageByUserIdAndSearch(Connection conn, Pageable page, Long id, String search) {
        log.info("Getting page of history orders by user id: {} and search: {}", id, search);
        try (PreparedStatement statement = conn.prepareStatement(
                historyOrderQueries.getSelectAllByUserIdAndSearchQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            log.info("Statement: {}", statement);
            statement.setFetchSize(page.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, id);
            statement.setString(2, "%" + search + "%");
            statement.setInt(3, page.getPageSize());
            statement.setLong(4, page.getOffset());
            try (ResultSet rs = statement.executeQuery()) {
                List<HistoryOrder> historyOrders = historyOrderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(historyOrders, page, rs.getLong(7));
            }
        } catch (SQLException e) {
            log.error("Error getting page of history orders by user id: {} and search: {}", e.getMessage());
            throw new DaoException("Error getting page of history orders by user id: " + e.getMessage(), e);
        }
    }
}
