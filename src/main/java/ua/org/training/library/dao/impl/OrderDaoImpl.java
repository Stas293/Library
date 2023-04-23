package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.postgres_queries.OrderQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.OrderDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Order;
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
public class OrderDaoImpl implements OrderDao {
    private final OrderQueries orderQueries;
    private final Collector<Order> orderCollector;

    @Autowired
    public OrderDaoImpl(OrderQueries orderQueries,
                        @Qualifier("orderCollector") Collector<Order> orderCollector) {
        this.orderQueries = orderQueries;
        this.orderCollector = orderCollector;
    }

    @Override
    public Order create(Connection connection, Order model) {
        log.info("Creating order: {}", model);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, model.getDateCreated());
            ps.setLong(2, model.getBook().getId());
            ps.setLong(3, model.getStatus().getId());
            ps.setLong(4, model.getUser().getId());
            ps.setLong(5, model.getPlace().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setId(rs.getLong(1));
                    return model;
                }
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error creating order: {}", e.getMessage());
            throw new DaoException("Error creating order: " + model, e);
        }
    }

    @Override
    public List<Order> create(Connection connection, List<Order> models) {
        log.info("Creating orders: {}", models);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            for (Order model : models) {
                ps.setObject(1, model.getDateCreated());
                ps.setLong(2, model.getBook().getId());
                ps.setLong(3, model.getStatus().getId());
                ps.setLong(4, model.getUser().getId());
                ps.setLong(5, model.getPlace().getId());
                ps.addBatch();
            }
            ps.executeBatch();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                for (Order model : models) {
                    if (rs.next()) {
                        model.setId(rs.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
            return models;
        } catch (SQLException e) {
            log.error("Error creating orders: {}", e.getMessage());
            throw new DaoException("Error creating orders: " + models, e);
        }
    }

    @Override
    public Optional<Order> getById(Connection connection, long id) {
        log.info("Getting order by id: {}", id);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getSelectByIdQuery())) {
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(orderCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting order by id: {}", e.getMessage());
            throw new DaoException("Error getting order by id: " + id, e);
        }
    }

    @Override
    public List<Order> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting orders by ids: {}", ids);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getSelectByIdsQuery(ids.size()))) {
            ps.setFetchSize(ids.size());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                ps.setLong(i + 1, ids.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return orderCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting orders by ids: {}", e.getMessage());
            throw new DaoException("Error getting orders by ids: " + ids, e);
        }
    }

    @Override
    public List<Order> getAll(Connection connection) {
        log.info("Getting all orders");
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getSelectAllQuery())) {
            ps.setFetchSize(100);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = ps.executeQuery()) {
                return orderCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all orders: {}", e.getMessage());
            throw new DaoException("Error getting all orders", e);
        }
    }

    @Override
    public List<Order> getAll(Connection connection, Sort sort) {
        log.info("Getting all orders with sort: {}", sort);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getSelectAllQuery(sort))) {
            ps.setFetchSize(100);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = ps.executeQuery()) {
                return orderCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all orders with sort: {}", e.getMessage());
            throw new DaoException("Error getting all orders with sort: " + sort, e);
        }
    }

    @Override
    public Page<Order> getPage(Connection connection, Pageable page) {
        log.info("Getting page of orders: {}", page);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getSelectAllQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setInt(1, page.getPageSize());
            ps.setLong(2, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders: {}", e.getMessage());
            throw new DaoException("Error getting page of orders: " + page, e);
        }
    }

    @Override
    public void update(Connection connection, Order entity) {
        log.info("Updating order: {}", entity);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getUpdateQuery())) {
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setObject(1, entity.getDateCreated());
            ps.setObject(2, entity.getDateExpire());
            ps.setLong(3, entity.getStatus().getId());
            ps.setLong(4, entity.getUser().getId());
            ps.setLong(5, entity.getBook().getId());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating order: {}", e.getMessage());
            throw new DaoException("Error updating order: " + entity, e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting order by id: {}", id);
        try (PreparedStatement ps = connection.prepareStatement(
                orderQueries.getDeleteQuery())) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting order by id: {}", e.getMessage());
            throw new DaoException("Error deleting order by id: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting orders");
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getCountQuery())) {
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting orders: {}", e.getMessage());
            throw new DaoException("Error counting orders", e);
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all orders");
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getDeleteAllQuery())) {
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all orders: {}", e.getMessage());
            throw new DaoException("Error deleting all orders", e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting orders by ids: {}", longs);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getDeleteByIdsQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                ps.setLong(i + 1, longs.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting orders by ids: {}", e.getMessage());
            throw new DaoException("Error deleting orders by ids: " + longs, e);
        }
    }

    @Override
    public void update(Connection conn, List<Order> entities) {
        log.info("Updating orders: {}", entities);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getUpdateQuery())) {
            for (Order entity : entities) {
                ps.setObject(1, entity.getDateCreated());
                ps.setObject(2, entity.getDateExpire());
                ps.setLong(3, entity.getStatus().getId());
                ps.setLong(4, entity.getUser().getId());
                ps.setLong(5, entity.getBook().getId());
                ps.setLong(6, entity.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating orders: {}", e.getMessage());
            throw new DaoException("Error updating orders: " + entities, e);
        }
    }

    @Override
    public Page<Order> getPageByBookId(Connection conn, Pageable page, Long bookId) {
        log.info("Getting page of orders by book id: {}", bookId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByBookIdQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, bookId);
            ps.setInt(2, page.getPageSize());
            ps.setLong(3, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by book id: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by book id: " + bookId, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusAndUserId(Connection conn, Pageable page, Long statusId, Long userId) {
        log.info("Getting page of orders by status id: {} and user id: {}", statusId, userId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusAndUserIdQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, statusId);
            ps.setLong(2, userId);
            ps.setInt(3, page.getPageSize());
            ps.setLong(4, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {} and user id: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + statusId + " and user id: " + userId, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusId(Connection conn, Pageable page, Long statusId) {
        log.info("Getting page of orders by status id: {}", statusId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusIdQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, statusId);
            ps.setInt(2, page.getPageSize());
            ps.setLong(3, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + statusId, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusAndUserAndSearch(Connection conn, Pageable page, Long statusId, Long userId, String search) {
        log.info("Getting page of orders by status id: {} and user id: {} and search: {}", statusId, userId, search);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusAndUserIdAndSearchQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, statusId);
            ps.setLong(2, userId);
            ps.setString(3, "%" + search + "%");
            ps.setInt(4, page.getPageSize());
            ps.setLong(5, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {} and user id: {} and search: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + statusId + " and user id: " + userId + " and search: " + search, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusAndSearch(Connection conn, Pageable page, Long id, String search) {
        log.info("Getting page of orders by status id: {} and search: {}", id, search);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusAndSearchQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, id);
            ps.setString(2, "%" + search + "%");
            ps.setInt(3, page.getPageSize());
            ps.setLong(4, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {} and search: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + id + " and search: " + search, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusAndPlaceAndSearch(Connection conn, Pageable page, Long statusOrderId, Long placeOrderId, String search) {
        log.info("Getting page of orders by status id: {} and place id: {} and search: {}", statusOrderId, placeOrderId, search);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusAndPlaceAndSearchQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, statusOrderId);
            ps.setLong(2, placeOrderId);
            ps.setString(3, "%" + search + "%");
            ps.setInt(4, page.getPageSize());
            ps.setLong(5, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {} and place id: {} and search: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + statusOrderId + " and place id: " + placeOrderId + " and search: " + search, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusAndPlace(Connection conn, Pageable page, Long statusOrderId, Long placeOrderId) {
        log.info("Getting page of orders by status id: {} and place id: {}", statusOrderId, placeOrderId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusAndPlaceQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, statusOrderId);
            ps.setLong(2, placeOrderId);
            ps.setInt(3, page.getPageSize());
            ps.setLong(4, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = orderCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(orders, page, rs.getLong(8));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {} and place id: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + statusOrderId + " and place id: " + placeOrderId, e);
        }
    }

    @Override
    public Optional<Order> getOrderByUserIdAndBookId(Connection conn, Long authorityUserId, long bookId) {
        log.info("Getting order by user id: {} and book id: {}", authorityUserId, bookId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectByUserIdAndBookIdQuery())) {
            ps.setLong(1, authorityUserId);
            ps.setLong(2, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(orderCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting order by user id: {} and book id: {}", authorityUserId, bookId, e);
            throw new DaoException("Error getting order by user id: " + authorityUserId + " and book id: " + bookId, e);
        }
    }

    @Override
    public List<Order> getOrdersByBookId(Connection conn, long id) {
        log.info("Getting orders by book id: {}", id);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectByBookIdQuery())) {
            ps.setFetchSize(100);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return orderCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting orders by book id: {}", id, e);
            throw new DaoException("Error getting orders by book id: " + id, e);
        }
    }
}
