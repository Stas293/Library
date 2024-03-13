package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.PlaceDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.enums.constants.PlaceQueries;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Place;
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
public class PlaceDaoImpl implements PlaceDao {
    private final PlaceQueries placeQueries;
    private final Collector<Place> placeCollector;

    @Autowired
    public PlaceDaoImpl(PlaceQueries placeQueries,
                        @Qualifier("placeCollector") Collector<Place> placeCollector) {
        this.placeQueries = placeQueries;
        this.placeCollector = placeCollector;
    }

    @Override
    public Place create(Connection connection, Place model) {
        log.info("Creating place: {}", model);
        try (PreparedStatement ps = connection.prepareStatement(
                placeQueries.getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getCode());
            ps.setInt(2, model.getDefaultDays());
            ps.setBoolean(3, model.getChoosable());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setId(rs.getLong(1));
                    return model;
                }
                throw new DaoException("Error creating order: " + model);
            }
        } catch (SQLException e) {
            log.error("Error creating order: {}", e.getMessage());
            throw new DaoException("Error creating order: " + model, e);
        }
    }

    @Override
    public List<Place> create(Connection connection, List<Place> models) {
        log.info("Creating places: {}", models);
        try (PreparedStatement ps = connection.prepareStatement(
                placeQueries.getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            for (Place model : models) {
                ps.setString(1, model.getCode());
                ps.setInt(2, model.getDefaultDays());
                ps.setBoolean(3, model.getChoosable());
                ps.addBatch();
            }
            ps.executeBatch();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                for (Place model : models) {
                    if (rs.next()) {
                        model.setId(rs.getLong(1));
                    } else {
                        throw new DaoException("Error creating order: " + model);
                    }
                }
                return models;
            }
        } catch (SQLException e) {
            log.error("Error creating order: {}", e.getMessage());
            throw new DaoException("Error creating order: " + models, e);
        }
    }

    @Override
    public Optional<Place> getById(Connection connection, long id) {
        log.info("Getting place by id: {}", id);
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getGetByIdQuery())) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(placeCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting place by id: {}", e.getMessage());
            throw new DaoException("Error getting place by id: " + id, e);
        }
    }

    @Override
    public List<Place> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting places by ids: {}", ids);
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getGetByIdsQuery(ids.size()))) {
            ps.setFetchSize(ids.size());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                ps.setLong(i + 1, ids.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return placeCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting places by ids: {}", e.getMessage());
            throw new DaoException("Error getting places by ids: " + ids, e);
        }
    }

    @Override
    public List<Place> getAll(Connection connection) {
        log.info("Getting all places");
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getGetAllQuery())) {
            ps.setFetchSize(100);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = ps.executeQuery()) {
                return placeCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all places: {}", e.getMessage());
            throw new DaoException("Error getting all places", e);
        }
    }

    @Override
    public List<Place> getAll(Connection connection, Sort sort) {
        log.info("Getting all places");
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getGetAllQuery(sort))) {
            ps.setFetchSize(100);
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = ps.executeQuery()) {
                return placeCollector.collectList(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting all places: {}", e.getMessage());
            throw new DaoException("Error getting all places", e);
        }
    }

    @Override
    public Page<Place> getPage(Connection connection, Pageable page) {
        log.info("Getting page of places");
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getGetPageQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ps.setFetchSize(page.getPageSize());
            ps.setFetchDirection(ResultSet.FETCH_FORWARD);
            ps.setInt(1, page.getPageSize());
            ps.setLong(2, page.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Place> content = placeCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(content, page, rs.getLong(5));
            }
        } catch (SQLException e) {
            log.error("Error getting page of places: {}", e.getMessage());
            throw new DaoException("Error getting page of places", e);
        }
    }

    @Override
    public void update(Connection connection, Place entity) {
        log.info("Updating place: {}", entity);
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getUpdateQuery())) {
            ps.setString(1, entity.getCode());
            ps.setInt(2, entity.getDefaultDays());
            ps.setBoolean(3, entity.getChoosable());
            ps.setLong(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating place: {}", e.getMessage());
            throw new DaoException("Error updating place: " + entity, e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting place by id: {}", id);
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getDeleteQuery())) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting place by id: {}", e.getMessage());
            throw new DaoException("Error deleting place by id: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting places");
        try (PreparedStatement ps = conn.prepareStatement(placeQueries.getCountQuery())) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new DaoException("Error counting places");
            }
        } catch (SQLException e) {
            log.error("Error counting places: {}", e.getMessage());
            throw new DaoException("Error counting places", e);
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all places");
        try (PreparedStatement ps = conn.prepareStatement(placeQueries.getDeleteAllQuery())) {
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all places: {}", e.getMessage());
            throw new DaoException("Error deleting all places", e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting all places");
        try (PreparedStatement ps = conn.prepareStatement(placeQueries.getDeleteAllQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                ps.setLong(i + 1, longs.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all places: {}", e.getMessage());
            throw new DaoException("Error deleting all places", e);
        }
    }

    @Override
    public void update(Connection conn, List<Place> entities) {
        log.info("Updating places: {}", entities);
        try (PreparedStatement ps = conn.prepareStatement(placeQueries.getUpdateQuery())) {
            for (Place place : entities) {
                ps.setString(1, place.getCode());
                ps.setInt(2, place.getDefaultDays());
                ps.setBoolean(3, place.getChoosable());
                ps.setLong(4, place.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating places: {}", e.getMessage());
            throw new DaoException("Error updating places: " + entities, e);
        }
    }

    @Override
    public Optional<Place> getByOrderId(Connection connection, Long id) {
        log.info("Getting place by order id: {}", id);
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getGetByOrderIdQuery())) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(placeCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting place by order id: {}", e.getMessage());
            throw new DaoException("Error getting place by order id: " + id, e);
        }
    }

    @Override
    public Optional<Place> getByCode(Connection connection, String name) {
        log.info("Getting place by name: {}", name);
        try (PreparedStatement ps = connection.prepareStatement(placeQueries.getGetByCodeQuery())) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(placeCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting place by name: {}", e.getMessage());
            throw new DaoException("Error getting place by name: " + name, e);
        }
    }
}
