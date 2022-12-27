package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.PlaceDao;
import ua.org.training.library.dao.collector.PlaceCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Place;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPlaceDao implements PlaceDao {
    //language=MySQL
    private static final String CREATE_PLACE = "INSERT INTO place_list (`name`) VALUES (?)";
    //language=MySQL
    private static final String GET_PLACE_BY_ID = "SELECT * FROM place_list WHERE id = ?";
    //language=MySQL
    private static final String GET_PLACES_PAGE = "CALL GET_PLACES_PAGE(?, ?, ?, ?);";
    //language=MySQL
    private static final String UPDATE_PLACE = "UPDATE place_list SET `name` = ? WHERE id = ?";
    //language=MySQL
    private static final String DELETE_PLACE = "DELETE FROM place_list WHERE id = ?";
    //language=MySQL
    private static final String GET_PLACE_BY_ORDER_ID =
            "SELECT place_list.* FROM place_list inner join " +
                    "order_list on place_list.id = order_list.place_id " +
                    "where order_list.id = ?";
    //language=MySQL
    private static final String GET_PLACE_BY_NAME = "SELECT * FROM place_list WHERE `name` = ?";
    private static final Logger LOGGER = LogManager.getLogger(JDBCPlaceDao.class);
    private final Connection connection;
    public JDBCPlaceDao(Connection connection) {
        this.connection = connection;
    }
    public long create(Place model) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_PLACE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, model.getName());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                connection.commit();
                return id;
            }
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't create place", e);
            throw new DaoException("Can't create place", e);
        } finally {
            connection.setAutoCommit(true);
        }
        return 0;
    }

    @Override
    public Optional<Place> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_PLACE_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new PlaceCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get place by id", e);
            throw new DaoException("Can't get place by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Place> getPage(Page<Place> page) {
        List<Place> places = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall(GET_PLACES_PAGE)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, page.getSearch());
            statement.setString(4, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    places.add(new PlaceCollector().collectFromResultSet(resultSet));
                }
            }
            page.setData(places);
            page.setElementsCount(places.size());
        } catch (SQLException e) {
            LOGGER.error("Can't get places page", e);
            throw new DaoException("Can't get places page", e);
        }
        return page;
    }

    @Override
    public void update(Place entity) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PLACE)) {
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't update place", e);
            throw new DaoException("Can't update place", e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PLACE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Can't delete place", e);
            throw new DaoException("Can't delete place", e);
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
    public Optional<Place> getByOrderId(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_PLACE_BY_ORDER_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new PlaceCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get place by order id", e);
            throw new DaoException("Can't get place by order id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Place> getByName(String name) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PLACE_BY_NAME)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new PlaceCollector().collectFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Can't get place by name", e);
            throw new DaoException("Can't get place by name", e);
        }
        return Optional.empty();
    }
}
