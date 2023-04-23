package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.postgres_queries.PlaceNameQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.PlaceNameDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.PlaceName;
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
public class PlaceNameDaoImpl implements PlaceNameDao {
    private final PlaceNameQueries placeNameQueries;
    private final Collector<PlaceName> placeNameCollector;

    @Autowired
    public PlaceNameDaoImpl(PlaceNameQueries placeNameQueries,
                            @Qualifier("placeNameCollector") Collector<PlaceName> placeNameCollector) {
        this.placeNameQueries = placeNameQueries;
        this.placeNameCollector = placeNameCollector;
    }

    @Override
    public PlaceName create(Connection connection, PlaceName model) {
        log.info("Creating PlaceName: {}", model);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getCreateQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getLang());
            statement.setString(2, model.getName());
            statement.setLong(3, model.getPlace().getId());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getLong(1));
                    return model;
                }
                throw new SQLException("Creating PlaceName failed, no ID obtained.");
            }
        } catch (Exception e) {
            log.error("Error creating PlaceName: {}", e.getMessage());
            throw new DaoException("Error creating PlaceName: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceName> create(Connection connection, List<PlaceName> models) {
        log.info("Creating PlaceNames: {}", models);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getCreateQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            for (PlaceName model : models) {
                statement.setString(1, model.getLang());
                statement.setString(2, model.getName());
                statement.setLong(3, model.getPlace().getId());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                for (PlaceName model : models) {
                    if (rs.next()) {
                        model.setId(rs.getLong(1));
                    } else {
                        throw new SQLException("Creating PlaceName failed, no ID obtained.");
                    }
                }
            }
            return models;
        } catch (Exception e) {
            log.error("Error creating PlaceNames: {}", e.getMessage());
            throw new DaoException("Error creating PlaceNames: " + e.getMessage());
        }
    }

    @Override
    public Optional<PlaceName> getById(Connection connection, long id) {
        log.info("Getting PlaceName by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getGetByIdQuery())) {
            statement.setLong(1, id);
            statement.executeQuery();
            try (ResultSet rs = statement.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(placeNameCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error getting PlaceName by id: {}", e.getMessage());
            throw new DaoException("Error getting PlaceName by id: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceName> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting PlaceNames by ids: {}", ids);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getGetByIdsQuery(ids.size()))) {
            statement.setFetchSize(ids.size());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }
            try (ResultSet rs = statement.executeQuery()) {
                return placeNameCollector.collectList(rs);
            }
        } catch (Exception e) {
            log.error("Error getting PlaceNames by ids: {}", e.getMessage());
            throw new DaoException("Error getting PlaceNames by ids: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceName> getAll(Connection connection) {
        log.info("Getting all PlaceNames");
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getGetAllQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return placeNameCollector.collectList(rs);
            }
        } catch (Exception e) {
            log.error("Error getting all PlaceNames: {}", e.getMessage());
            throw new DaoException("Error getting all PlaceNames: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceName> getAll(Connection connection, Sort sort) {
        log.info("Getting all PlaceNames with sort: {}", sort);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getGetAllQuery(sort),
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return placeNameCollector.collectList(rs);
            }
        } catch (Exception e) {
            log.error("Error getting all PlaceNames with sort: {}", e.getMessage());
            throw new DaoException("Error getting all PlaceNames with sort: " + e.getMessage());
        }
    }

    @Override
    public Page<PlaceName> getPage(Connection connection, Pageable page) {
        log.info("Getting PlaceNames page: {}", page);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getGetPageQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(page.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (ResultSet rs = statement.executeQuery()) {
                List<PlaceName> content = placeNameCollector.collectList(rs);
                rs.last();
                if (rs.getRow() < 1) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(content, page, rs.getLong(5));
            }
        } catch (Exception e) {
            log.error("Error getting PlaceNames page: {}", e.getMessage());
            throw new DaoException("Error getting PlaceNames page: " + e.getMessage());
        }
    }

    @Override
    public void update(Connection connection, PlaceName entity) {
        log.info("Updating PlaceName: {}", entity);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getUpdateQuery())) {
            statement.setString(1, entity.getLang());
            statement.setString(2, entity.getName());
            statement.setLong(3, entity.getPlace().getId());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            log.error("Error updating PlaceName: {}", e.getMessage());
            throw new DaoException("Error updating PlaceName: " + e.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting PlaceName by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getDeleteQuery())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error("Error deleting PlaceName by id: {}", e.getMessage());
            throw new DaoException("Error deleting PlaceName by id: " + e.getMessage());
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting PlaceNames");
        try (PreparedStatement statement = conn.prepareStatement(
                placeNameQueries.getCountQuery())) {
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0;
        } catch (Exception e) {
            log.error("Error counting PlaceNames: {}", e.getMessage());
            throw new DaoException("Error counting PlaceNames: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all PlaceNames");
        try (PreparedStatement statement = conn.prepareStatement(
                placeNameQueries.getDeleteAllQuery())) {
            statement.executeUpdate();
        } catch (Exception e) {
            log.error("Error deleting all PlaceNames: {}", e.getMessage());
            throw new DaoException("Error deleting all PlaceNames: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting PlaceNames by ids: {}", longs);
        try (PreparedStatement statement = conn.prepareStatement(
                placeNameQueries.getDeleteAllQuery(longs))) {
            statement.executeUpdate();
        } catch (Exception e) {
            log.error("Error deleting PlaceNames by ids: {}", e.getMessage());
            throw new DaoException("Error deleting PlaceNames by ids: " + e.getMessage());
        }
    }

    @Override
    public void update(Connection conn, List<PlaceName> entities) {
        log.info("Updating PlaceNames: {}", entities);
        try (PreparedStatement statement = conn.prepareStatement(
                placeNameQueries.getUpdateQuery())) {
            for (PlaceName entity : entities) {
                statement.setString(1, entity.getLang());
                statement.setString(2, entity.getName());
                statement.setLong(3, entity.getPlace().getId());
                statement.setLong(4, entity.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (Exception e) {
            log.error("Error updating PlaceNames: {}", e.getMessage());
            throw new DaoException("Error updating PlaceNames: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceName> getAllByPlaceId(Connection connection, Long id) {
        log.info("Getting all PlaceNames by Place id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getGetAllByPlaceIdQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return placeNameCollector.collectList(rs);
            }
        } catch (Exception e) {
            log.error("Error getting all PlaceNames by Place id: {}", e.getMessage());
            throw new DaoException("Error getting all PlaceNames by Place id: " + e.getMessage());
        }
    }

    @Override
    public Optional<PlaceName> getByPlaceId(Connection connection, Long placeId, Locale locale) {
        log.info("Getting PlaceName by Place id: {} and locale: {}", placeId, locale);
        try (PreparedStatement statement = connection.prepareStatement(
                placeNameQueries.getGetByPlaceIdAndLocaleQuery())) {
            statement.setLong(1, placeId);
            statement.setString(2, locale.getLanguage());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(placeNameCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error getting PlaceName by Place id: {} and locale: {}", e.getMessage());
            throw new DaoException("Error getting PlaceName by Place id: " + e.getMessage());
        }
    }
}
