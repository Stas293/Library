package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.HistoryOrderDao;
import ua.org.training.library.dao.collector.HistoryOrderCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCHistoryOrderDao implements HistoryOrderDao {
    //language=MySQL
    private static final String INSERT_HISTORY_ORDER =
            "INSERT INTO history_order (user_id, date_created, date_expire, book_name, status_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String GET_HISTORY_ORDER_BY_ID = "SELECT * FROM history_order WHERE id = ?";
    //language=MySQL
    private static final String GET_PAGE_HISTORY_ORDER = "CALL GET_PAGE_HISTORY_ORDER(?, ?, ?, ?);";
    //language=MySQL
    private static final String UPDATE_HISTORY_ORDER = "UPDATE history_order SET user_id = ?, date_created = ?, " +
            "date_expire = ?, book_name = ? WHERE id = ?";
    //language=MySQL
    private static final String DELETE_HISTORY_ORDER = "DELETE FROM history_order WHERE id = ?";
    //language=MySQL
    private static final String GET_PAGE_HISTORY_ORDER_BY_USER_ID = "CALL GET_PAGE_HISTORY_ORDER_BY_USER_ID(?, ?, ?, ?, ?);";
    //language=MySQL
    private static final String COUNT_HISTORY_ORDER = "SELECT COUNT(*) FROM history_order";
    //language=MySQL
    private static final String COUNT_HISTORY_ORDER_BY_USER_ID = "SELECT COUNT(*) FROM history_order WHERE user_id = ?";
    private static final Logger LOGGER = LogManager.getLogger(JDBCHistoryOrderDao.class);
    private final Connection connection;

    public JDBCHistoryOrderDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long create(HistoryOrder model) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(INSERT_HISTORY_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, model.getUser().getId());
            statement.setTimestamp(2, new Timestamp(model.getDateCreated().getTime()));
            statement.setTimestamp(3, new Timestamp(model.getDateExpire().getTime()));
            statement.setString(4, model.getBookName());
            statement.setLong(5, model.getStatus().getId());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    connection.commit();
                    return id;
                }
            }
            connection.rollback();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Cannot create history order", e);
            throw new DaoException("Cannot create history order", e);
        } finally {
            connection.setAutoCommit(true);
        }
        return 0;
    }

    @Override
    public Optional<HistoryOrder> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_HISTORY_ORDER_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    HistoryOrderCollector collector = new HistoryOrderCollector();
                    return Optional.of(collector.collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Cannot get history order by id", e);
            throw new DaoException("Cannot get history order by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Page<HistoryOrder> getPage(Page<HistoryOrder> page) {
        try (CallableStatement statement = connection.prepareCall(GET_PAGE_HISTORY_ORDER)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, page.getSearch());
            statement.setString(4, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                List<HistoryOrder> historyOrders = new ArrayList<>();
                HistoryOrderCollector collector = new HistoryOrderCollector();
                while (resultSet.next()) {
                    historyOrders.add(collector.collectFromResultSet(resultSet));
                }
                page.setData(historyOrders);
                try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_HISTORY_ORDER)) {
                    try (ResultSet resultSet1 = preparedStatement.executeQuery()) {
                        if (resultSet1.next()) {
                            page.setElementsCount(resultSet1.getLong(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Cannot get page history order", e);
            throw new DaoException("Cannot get page history order", e);
        }
        return page;
    }

    @Override
    public void update(HistoryOrder entity) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_HISTORY_ORDER)) {
            statement.setLong(1, entity.getUser().getId());
            statement.setTimestamp(2, new Timestamp(entity.getDateCreated().getTime()));
            statement.setTimestamp(3, new Timestamp(entity.getDateExpire().getTime()));
            statement.setString(4, entity.getBookName());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Cannot update history order", e);
            throw new DaoException("Cannot update history order", e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_HISTORY_ORDER)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Cannot delete history order", e);
            throw new DaoException("Cannot delete history order", e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Cannot close connection", e);
            throw new DaoException("Cannot close connection", e);
        }
    }

    @Override
    public Page<HistoryOrder> getPageByUserId(Page<HistoryOrder> page, Long userId) {
        try (CallableStatement statement = connection.prepareCall(GET_PAGE_HISTORY_ORDER_BY_USER_ID)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setLong(3, userId);
            statement.setString(4, page.getSearch());
            statement.setString(5, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                List<HistoryOrder> historyOrders = new ArrayList<>();
                HistoryOrderCollector collector = new HistoryOrderCollector();
                while (resultSet.next()) {
                    historyOrders.add(collector.collectFromResultSet(resultSet));
                }
                page.setData(historyOrders);
                try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_HISTORY_ORDER_BY_USER_ID)) {
                    preparedStatement.setLong(1, userId);
                    try (ResultSet resultSet1 = preparedStatement.executeQuery()) {
                        if (resultSet1.next()) {
                            page.setElementsCount(resultSet1.getLong(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Cannot get page history order by user id", e);
            throw new DaoException("Cannot get page history order by user id", e);
        }
        return page;
    }
}
