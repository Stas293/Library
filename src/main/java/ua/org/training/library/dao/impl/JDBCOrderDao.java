package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.OrderDao;
import ua.org.training.library.dao.collector.OrderCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Order;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCOrderDao implements OrderDao {
    //language=MySQL
    private static final String CREATE_ORDER = 
            "INSERT INTO order_list (date_created, book_id, user_id, place_id, status_id) VALUES (?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String GET_ORDER_BY_ID = "SELECT * FROM order_list WHERE id = ?";
    //language=MySQL
    private static final String GET_ORDERS_PAGE = "CALL GET_ORDERS_PAGE(?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_ORDERS_COUNT = "SELECT COUNT(*) FROM order_list";
    //language=MySQL
    private static final String UPDATE_ORDER =
            "UPDATE order_list SET date_expire = ?, status_id = ? WHERE id = ?";
    //language=MySQL
    private static final String DELETE_ORDER = "DELETE FROM order_list WHERE id = ?";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_USER_ID = "CALL GET_ORDERS_PAGE_BY_USER_ID(?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_PLACE_ID = "CALL GET_ORDERS_PAGE_BY_PLACE_ID(?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_USER_ID_AND_PLACE_ID =
            "CALL GET_ORDERS_PAGE_BY_USER_ID_AND_PLACE_ID(?, ?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_PLACE_NAME = "CALL GET_ORDERS_PAGE_BY_PLACE_NAME(?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_BOOK_ID = "CALL GET_ORDERS_PAGE_BY_BOOK_ID(?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_BOOK_COUNT_BY_ID = "SELECT book_count FROM books_catalog WHERE book_id = ?";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_STATUS_AND_USER_ID =
            "CALL GET_ORDERS_PAGE_BY_STATUS_AND_USER_ID(?, ?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String CHANGE_BOOK_COUNT = "UPDATE books_catalog SET book_count = ? WHERE book_id = ?";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_STATUS_ID = "CALL GET_ORDERS_PAGE_BY_STATUS_ID(?, ?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_ORDERS_COUNT_BY_STATUS_ID = "SELECT COUNT(*) FROM order_list WHERE status_id = ?";
    //language=MySQL
    private static final String GET_ORDERS_PAGE_BY_STATUS_ID_AND_PLACE_ID =
            "CALL GET_ORDERS_PAGE_BY_STATUS_AND_PLACE_ID(?, ?, ?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String GET_ORDERS_COUNT_BY_STATUS_ID_AND_PLACE_ID =
            "SELECT COUNT(*) FROM order_list WHERE status_id = ? AND place_id = ?";
    //language=MySQL
    private static final String GET_ORDERS_COUNT_BY_BOOK_ID = "SELECT COUNT(*) FROM order_list WHERE book_id = ?";
    //language=MySQL
    private static final String GET_ORDERS_COUNT_BY_STATUS_AND_USER_ID = "SELECT COUNT(*) FROM order_list WHERE status_id = ? AND user_id = ?";
    private static final Logger LOGGER = LogManager.getLogger(JDBCOrderDao.class);
    private final Connection connection;
    public JDBCOrderDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long create(Order model) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setTimestamp(1, new Timestamp(model.getDateCreated().getTime()));
            statement.setLong(2, model.getBook().getId());
            statement.setLong(3, model.getUser().getId());
            statement.setLong(4, model.getPlace().getId());
            statement.setLong(5, model.getStatus().getId());
            try (PreparedStatement statement1 = connection.prepareStatement(GET_BOOK_COUNT_BY_ID)) {
                statement1.setLong(1, model.getBook().getId());
                try (ResultSet resultSet = statement1.executeQuery()) {
                    if (resultSet.next()) {
                        int bookCount = resultSet.getInt("book_count");
                        LOGGER.debug("Book count: {}", bookCount);
                        if (bookCount > 0) {
                            bookCount--;
                            try (PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_BOOK_COUNT)) {
                                preparedStatement.setInt(1, bookCount);
                                preparedStatement.setLong(2, model.getBook().getId());
                                preparedStatement.executeUpdate();
                            }
                            statement.executeUpdate();
                            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    long id = generatedKeys.getLong(1);
                                    connection.commit();
                                    return id;
                                }
                            }
                        }
                        connection.rollback();
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't create order", e);
            throw new DaoException("Can't create order", e);
        } finally {
            connection.setAutoCommit(true);
        }
        return Constants.INVALID_ID;
    }

    @Override
    public Optional<Order> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_ORDER_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new OrderCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get order by id", e);
            throw new DaoException("Can't get order by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Order> getPage(Page<Order> page) {
        try (CallableStatement statement = connection.prepareCall(GET_ORDERS_PAGE)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, page.getSearch());
            statement.setString(4, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                while (resultSet.next()) {
                    orders.add(new OrderCollector().collectFromResultSet(resultSet));
                }
                page.setData(orders);
                try (PreparedStatement statement1 = connection.prepareStatement(GET_ORDERS_COUNT)) {
                    try (ResultSet resultSet1 = statement1.executeQuery()) {
                        if (resultSet1.next()) {
                            page.setElementsCount(resultSet1.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get orders page", e);
            throw new DaoException("Can't get orders page", e);
        }
        return page;
    }

    @Override
    public void update(Order entity) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER)) {
            statement.setTimestamp(1, new Timestamp(entity.getDateExpire().getTime()));
            statement.setLong(2, entity.getStatus().getId());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't update order", e);
            throw new DaoException("Can't update order", e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ORDER)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't delete order", e);
            throw new DaoException("Can't delete order", e);
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
    public Page<Order> getPageByBookId(Page<Order> page, Long bookId) {
        try (CallableStatement statement = connection.prepareCall(GET_ORDERS_PAGE_BY_BOOK_ID)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setLong(3, bookId);
            statement.setString(4, page.getSearch());
            statement.setString(5, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                OrderCollector orderCollector = new OrderCollector();
                while (resultSet.next()) {
                    orders.add(orderCollector.collectFromResultSet(resultSet));
                }
                page.setData(orders);
                try (PreparedStatement statement1 = connection.prepareStatement(GET_ORDERS_COUNT_BY_BOOK_ID)) {
                    statement1.setLong(1, bookId);
                    try (ResultSet resultSet1 = statement1.executeQuery()) {
                        if (resultSet1.next()) {
                            page.setElementsCount(resultSet1.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get orders page by book id", e);
            throw new DaoException("Can't get orders page by book id", e);
        }
        return page;
    }

    @Override
    public Page<Order> getPageByStatusAndUserId(Page<Order> page, Long statusId, Long userId) {
        try (CallableStatement statement = connection.prepareCall(GET_ORDERS_PAGE_BY_STATUS_AND_USER_ID)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setLong(3, userId);
            statement.setLong(4, statusId);
            statement.setString(5, page.getSearch());
            statement.setString(6, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                OrderCollector orderCollector = new OrderCollector();
                while (resultSet.next()) {
                    orders.add(orderCollector.collectFromResultSet(resultSet));
                }
                page.setData(orders);
                try (PreparedStatement statement1 = connection.prepareStatement(GET_ORDERS_COUNT_BY_STATUS_AND_USER_ID)) {
                    statement1.setLong(1, statusId);
                    statement1.setLong(2, userId);
                    try (ResultSet resultSet1 = statement1.executeQuery()) {
                        if (resultSet1.next()) {
                            page.setElementsCount(resultSet1.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get orders page by status and user id", e);
            throw new DaoException("Can't get orders page by status and user id", e);
        }
        return page;
    }

    @Override
    public Page<Order> getPageByStatusId(Page<Order> page, Long statusId, String sortBy) {
        try (CallableStatement statement = connection.prepareCall(GET_ORDERS_PAGE_BY_STATUS_ID)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setLong(3, statusId);
            statement.setString(4, page.getSearch());
            statement.setString(5, page.getSorting());
            statement.setString(6, sortBy);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                OrderCollector orderCollector = new OrderCollector();
                while (resultSet.next()) {
                    orders.add(orderCollector.collectFromResultSet(resultSet));
                }
                page.setData(orders);
                try (PreparedStatement statementCount = connection.prepareStatement(GET_ORDERS_COUNT_BY_STATUS_ID)) {
                    statementCount.setLong(1, statusId);
                    try (ResultSet resultSetCount = statementCount.executeQuery()) {
                        if (resultSetCount.next()) {
                            page.setElementsCount(resultSetCount.getLong(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get orders page by status id", e);
            throw new DaoException("Can't get orders page by status id", e);
        }
        return page;
    }

    @Override
    public Page<Order> getPageByStatusIdAndPlaceId(Page<Order> page, Long statusId, Long placeId, String sortBy) {
        try (PreparedStatement preparedStatement = connection.prepareCall(GET_ORDERS_PAGE_BY_STATUS_ID_AND_PLACE_ID)) {
            preparedStatement.setLong(1, page.getLimit());
            preparedStatement.setLong(2, page.getOffset());
            preparedStatement.setLong(3, placeId);
            preparedStatement.setLong(4, statusId);
            preparedStatement.setString(5, page.getSearch());
            preparedStatement.setString(6, page.getSorting());
            preparedStatement.setString(7, sortBy);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                OrderCollector orderCollector = new OrderCollector();
                while (resultSet.next()) {
                    orders.add(orderCollector.collectFromResultSet(resultSet));
                }
                page.setData(orders);
                try (PreparedStatement statementCount = connection.prepareStatement(GET_ORDERS_COUNT_BY_STATUS_ID_AND_PLACE_ID)) {
                    statementCount.setLong(1, statusId);
                    statementCount.setLong(2, placeId);
                    try (ResultSet resultSetCount = statementCount.executeQuery()) {
                        if (resultSetCount.next()) {
                            page.setElementsCount(resultSetCount.getLong(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get orders page by status id and place id", e);
            throw new DaoException("Can't get orders page by status id and place id", e);
        }
        return page;
    }

}
