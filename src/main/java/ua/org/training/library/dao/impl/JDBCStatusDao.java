package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.collector.StatusCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Status;
import ua.org.training.library.utility.page.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCStatusDao implements StatusDao {
    private static final Logger LOGGER = LogManager.getLogger(JDBCStatusDao.class);
    private final Connection connection;
    public JDBCStatusDao(Connection connection) {
        this.connection = connection;
    }

    //language=MySQL
    private static final String GET_STATUS_BY_ID = "SELECT * FROM status WHERE id = ?";
    //language=MySQL
    private static final String UPDATE_STATUS_BY_ID = "UPDATE status SET code = ?, name = ?, closed = ? WHERE id = ?";
    //language=MySQL
    private static final String DELETE_STATUS_BY_ID = "DELETE FROM status WHERE id = ?";
    //language=MySQL
    private static final String GET_STATUS_BY_CODE = "SELECT * FROM status WHERE code = ?";
    //language=MySQL
    private static final String GET_STATUS_BY_ORDER_ID =
            "SELECT s.* FROM status s inner join order_list o on o.status_id = s.id WHERE o.id = ?";
    //language=MySQL
    private static final String GET_NEXT_STATUSES_BY_STATUS_ID =
            " select * from next_statuses ns inner join status s on ns.nextstatus = s.id where ns.status = ? ";
    //language=MySQL
    private static final String GET_STATUS_BY_HISTORY_ORDER_ID =
            "SELECT s.* FROM status s inner join history_order o on o.status_id = s.id WHERE o.id = ?";

    @Override
    public long create(Status model) throws SQLException {
        return 0;
    }

    @Override
    public Optional<Status> getById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STATUS_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new StatusCollector().collectFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get status by id", e);
            throw new DaoException("Can't get status by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Status> getPage(Page<Status> page) {
        return null;
    }

    @Override
    public void update(Status entity) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS_BY_ID)) {
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setBoolean(3, entity.isClosed());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Can't update status", e);
            throw new DaoException("Can't update status", e);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATUS_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Can't delete status", e);
            throw new DaoException("Can't delete status", e);
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
    public Optional<Status> getByCode(String code) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STATUS_BY_CODE)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new StatusCollector().collectFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get status by code", e);
            throw new DaoException("Can't get status by code", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Status> getByOrderId(long orderId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STATUS_BY_ORDER_ID)) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new StatusCollector().collectFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get status by order id", e);
            throw new DaoException("Can't get status by order id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Status> getByHistoryOrderId(long historyId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STATUS_BY_HISTORY_ORDER_ID)) {
            preparedStatement.setLong(1, historyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new StatusCollector().collectFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get status by history order id", e);
            throw new DaoException("Can't get status by history order id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Status> getNextStatusesForStatusById(Long id) {
        List<Status> statuses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_NEXT_STATUSES_BY_STATUS_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                statuses.add(new StatusCollector().collectFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get next statuses by status id", e);
            throw new DaoException("Can't get next statuses by status id", e);
        }
        return statuses;
    }
}
