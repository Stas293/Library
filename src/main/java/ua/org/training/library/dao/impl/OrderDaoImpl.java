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
            ps.setDate(1, Date.valueOf(model.getDateCreated()));
            ps.setLong(2, model.getBook().getId());
            ps.setLong(3, model.getStatus().getId());
            ps.setLong(4, model.getUser().getId());
            ps.setLong(5, model.getPlace().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setId(rs.getLong(1));
                    return model;
                } else {
                    throw new DaoException("Error creating order: " + model);
                }
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
                ps.setDate(1, Date.valueOf(model.getDateCreated()));
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
                        throw new DaoException("Error creating orders: " + models);
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
                orderQueries.getSelectAllQuery(page))) {
            ps.setInt(1, page.getPageSize());
            ps.setLong(2, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                return new PageImpl<>(orderCollector.collectList(rs), page, count(connection));
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
            ps.setDate(1, Date.valueOf(entity.getDateCreated()));
            ps.setDate(2, Date.valueOf(entity.getDateExpire()));
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
                ps.setDate(1, Date.valueOf(entity.getDateCreated()));
                ps.setDate(2, Date.valueOf(entity.getDateExpire()));
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
                orderQueries.getSelectAllByBookIdQuery(page))) {
            ps.setLong(1, bookId);
            ps.setLong(2, page.getPageSize());
            ps.setLong(3, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                return new PageImpl<>(orderCollector.collectList(rs), page, countByBookId(conn, bookId));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by book id: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by book id: " + bookId, e);
        }
    }

    private long countByBookId(Connection conn, Long bookId) {
        log.info("Counting orders by book id: {}", bookId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getCountByBookIdQuery())) {
            ps.setLong(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting orders by book id: {}", e.getMessage());
            throw new DaoException("Error counting orders by book id: " + bookId, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusAndUserId(Connection conn, Pageable page, Long statusId, Long userId) {
        log.info("Getting page of orders by status id: {} and user id: {}", statusId, userId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusAndUserIdQuery(page))) {
            ps.setLong(1, statusId);
            ps.setLong(2, userId);
            ps.setLong(3, page.getPageSize());
            ps.setLong(4, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                return new PageImpl<>(orderCollector.collectList(rs), page, countByStatusAndUserId(conn, statusId, userId));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {} and user id: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + statusId + " and user id: " + userId, e);
        }
    }

    private long countByStatusAndUserId(Connection conn, Long statusId, Long userId) {
        log.info("Counting orders by status id: {} and user id: {}", statusId, userId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getCountByStatusAndUserIdQuery())) {
            ps.setLong(1, statusId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting orders by status id: {} and user id: {}", e.getMessage());
            throw new DaoException("Error counting orders by status id: " + statusId + " and user id: " + userId, e);
        }
    }

    @Override
    public Page<Order> getPageByStatusId(Connection conn, Pageable page, Long statusId) {
        log.info("Getting page of orders by status id: {}", statusId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getSelectAllByStatusIdQuery(page))) {
            ps.setLong(1, statusId);
            ps.setLong(2, page.getPageSize());
            ps.setLong(3, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                return new PageImpl<>(orderCollector.collectList(rs), page, countByStatusId(conn, statusId));
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
                orderQueries.getSelectAllByStatusAndUserIdAndSearchQuery(page))) {
            ps.setLong(1, statusId);
            ps.setLong(2, userId);
            ps.setString(3, "%" + search + "%");
            ps.setLong(4, page.getPageSize());
            ps.setLong(5, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                return new PageImpl<>(orderCollector.collectList(rs), page, countByStatusAndUserAndSearch(conn, statusId, userId, search));
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
                orderQueries.getSelectAllByStatusAndSearchQuery(page))) {
            ps.setLong(1, id);
            ps.setString(2, "%" + search + "%");
            ps.setLong(3, page.getPageSize());
            ps.setLong(4, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                return new PageImpl<>(orderCollector.collectList(rs), page, countByStatusAndSearch(conn, id, search));
            }
        } catch (SQLException e) {
            log.error("Error getting page of orders by status id: {} and search: {}", e.getMessage());
            throw new DaoException("Error getting page of orders by status id: " + id + " and search: " + search, e);
        }
    }

    private long countByStatusAndSearch(Connection conn, Long id, String search) {
        log.info("Counting orders by status id: {} and search: {}", id, search);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getCountByStatusAndSearchQuery())) {
            ps.setLong(1, id);
            ps.setString(2, "%" + search + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting orders by status id: {} and search: {}", e.getMessage());
            throw new DaoException("Error counting orders by status id: " + id + " and search: " + search, e);
        }
    }

    private long countByStatusAndUserAndSearch(Connection conn, Long statusId, Long userId, String search) {
        log.info("Counting orders by status id: {} and user id: {} and search: {}", statusId, userId, search);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getCountByStatusAndUserIdAndSearchQuery())) {
            ps.setLong(1, statusId);
            ps.setLong(2, userId);
            ps.setString(3, "%" + search + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting orders by status id: {} and user id: {} and search: {}", e.getMessage());
            throw new DaoException("Error counting orders by status id: " + statusId + " and user id: " + userId + " and search: " + search, e);
        }
    }

    private long countByStatusId(Connection conn, Long statusId) {
        log.info("Counting orders by status id: {}", statusId);
        try (PreparedStatement ps = conn.prepareStatement(
                orderQueries.getCountByStatusIdQuery())) {
            ps.setLong(1, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting orders by status id: {}", e.getMessage());
            throw new DaoException("Error counting orders by status id: " + statusId, e);
        }
    }
}
