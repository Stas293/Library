package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.postgres_queries.KeywordQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.KeywordDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Keyword;
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
public class KeywordDaoImpl implements KeywordDao {
    private final KeywordQueries keywordQueries;
    private final Collector<Keyword> keywordCollector;

    @Autowired
    public KeywordDaoImpl(KeywordQueries keywordQueries,
                          @Qualifier("keywordCollector") Collector<Keyword> keywordCollector) {
        this.keywordQueries = keywordQueries;
        this.keywordCollector = keywordCollector;
    }

    @Override
    public Keyword create(Connection connection, Keyword model) {
        log.info("Creating keyword: {}", model);
        try (var statement = connection.prepareStatement(
                keywordQueries.getInsertQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getData());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setId(rs.getLong(1));
                    return model;
                }
                throw new SQLException("Error creating keyword: no ID obtained.");
            }
        } catch (SQLException ex) {
            log.error("Error creating keyword: {}", ex.getMessage());
            throw new DaoException("Error creating keyword: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Keyword> create(Connection connection, List<Keyword> models) {
        log.info("Creating keywords: {}", models);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getInsertQuery(),
                Statement.RETURN_GENERATED_KEYS)) {
            for (Keyword model : models) {
                statement.setString(1, model.getData());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                for (Keyword model : models) {
                    if (rs.next()) {
                        model.setId(rs.getLong(1));
                    } else {
                        throw new SQLException("Error creating keyword: no ID obtained.");
                    }
                }
            }
            return models;
        } catch (SQLException ex) {
            log.error("Error creating keyword: {}", ex.getMessage());
            throw new DaoException("Error creating keyword: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Keyword> getById(Connection connection, long id) {
        log.info("Getting keyword by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getSelectByIdQuery())) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(keywordCollector.collect(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            log.error("Error getting keyword by id: {}", ex.getMessage());
            throw new DaoException("Error getting keyword by id: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Keyword> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting keywords by ids: {}", ids);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getSelectByIdsQuery(ids.size()))) {
            statement.setFetchSize(ids.size());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }
            try (ResultSet rs = statement.executeQuery()) {
                return keywordCollector.collectList(rs);
            }
        } catch (SQLException ex) {
            log.error("Error getting keywords by ids: {}", ex.getMessage());
            throw new DaoException("Error getting keywords by ids: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Keyword> getAll(Connection connection) {
        log.info("Getting all keywords");
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getSelectAllQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return keywordCollector.collectList(rs);
            }
        } catch (SQLException ex) {
            log.error("Error getting all keywords: {}", ex.getMessage());
            throw new DaoException("Error getting all keywords: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Keyword> getAll(Connection connection, Sort sort) {
        log.info("Getting all keywords");
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getSelectAllQuery(sort))) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                return keywordCollector.collectList(rs);
            }
        } catch (SQLException ex) {
            log.error("Error getting all keywords: {}", ex.getMessage());
            throw new DaoException("Error getting all keywords: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Page<Keyword> getPage(Connection connection, Pageable page) {
        log.info("Getting page of keywords");
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getSelectAllQuery(page),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(page.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (ResultSet rs = statement.executeQuery()) {
                List<Keyword> keywords = keywordCollector.collectList(rs);
                rs.last();
                if (rs.getRow() == 0) {
                    return new PageImpl<>(Collections.emptyList(), page, 0);
                }
                return new PageImpl<>(keywords, page, rs.getLong(3));
            }
        } catch (SQLException ex) {
            log.error("Error getting page of keywords: {}", ex.getMessage());
            throw new DaoException("Error getting page of keywords: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(Connection connection, Keyword entity) {
        log.info("Updating keyword: {}", entity);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getUpdateQuery())) {
            statement.setString(1, entity.getData());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Error updating keyword: {}", ex.getMessage());
            throw new DaoException("Error updating keyword: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting keyword by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getDeleteByIdQuery())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Error deleting keyword by id: {}", ex.getMessage());
            throw new DaoException("Error deleting keyword by id: " + ex.getMessage(), ex);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting keywords");
        try (PreparedStatement statement = conn.prepareStatement(
                keywordQueries.getCountQuery())) {
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Error counting keywords: no result obtained.");
                }
            }
        } catch (SQLException ex) {
            log.error("Error counting keywords: {}", ex.getMessage());
            throw new DaoException("Error counting keywords: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all keywords");
        try (PreparedStatement statement = conn.prepareStatement(
                keywordQueries.getDeleteAllQuery())) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Error deleting all keywords: {}", ex.getMessage());
            throw new DaoException("Error deleting all keywords: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting keywords by ids: {}", longs);
        try (PreparedStatement statement = conn.prepareStatement(
                keywordQueries.getDeleteByIdsQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                statement.setLong(i + 1, longs.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Error deleting keywords by ids: {}", ex.getMessage());
            throw new DaoException("Error deleting keywords by ids: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(Connection conn, List<Keyword> entities) {
        log.info("Updating keywords: {}", entities);
        try (PreparedStatement statement = conn.prepareStatement(
                keywordQueries.getUpdateQuery())) {
            for (Keyword entity : entities) {
                statement.setString(1, entity.getData());
                statement.setLong(2, entity.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException ex) {
            log.error("Error updating keywords: {}", ex.getMessage());
            throw new DaoException("Error updating keywords: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Keyword> getKeywordsByBookId(Connection connection, Long id) {
        log.info("Getting keywords by book id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getSelectByBookIdQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return keywordCollector.collectList(rs);
            }
        } catch (SQLException ex) {
            log.error("Error getting keywords by book id: {}", ex.getMessage());
            throw new DaoException("Error getting keywords by book id: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void setKeywordsToBook(Connection connection, Long bookId, List<Keyword> keywords) {
        log.info("Setting keywords to book: {} {}", bookId, keywords);
        deleteKeywordsByBookId(connection, bookId);
        reSetKeywordsToBook(connection, bookId, keywords);
    }

    @Override
    public List<Keyword> getKeywordsByQuery(Connection conn, String query) {
        log.info("Getting keywords by query: {}", query);
        try (PreparedStatement statement = conn.prepareStatement(
                keywordQueries.getSelectByQueryQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setQueryTimeout(5);
            statement.setString(1, "%" + query + "%");
            try (ResultSet rs = statement.executeQuery()) {
                return keywordCollector.collectList(rs);
            }
        } catch (SQLException ex) {
            log.error("Error getting keywords by query: {}", ex.getMessage());
            throw new DaoException("Error getting keywords by query: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Keyword> getByData(Connection conn, String keyword) {
        log.info("Getting keyword by data: {}", keyword);
        try (PreparedStatement statement = conn.prepareStatement(
                keywordQueries.getSelectByDataQuery())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setQueryTimeout(5);
            statement.setString(1, keyword);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(keywordCollector.collect(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException ex) {
            log.error("Error getting keyword by data: {}", ex.getMessage());
            throw new DaoException("Error getting keyword by data: " + ex.getMessage(), ex);
        }
    }

    private void reSetKeywordsToBook(Connection connection, Long bookId, List<Keyword> keywords) {
        log.info("Re-setting keywords to book: {}", keywords);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getInserKeywordToBookQuery())) {
            for (Keyword keyword : keywords) {
                statement.setLong(1, bookId);
                statement.setLong(2, keyword.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException ex) {
            log.error("Error re-setting keywords to book: {}", ex.getMessage());
            throw new DaoException("Error re-setting keywords to book: " + ex.getMessage(), ex);
        }
    }

    private void deleteKeywordsByBookId(Connection connection, Long bookId) {
        log.info("Deleting keywords by book id: {}", bookId);
        try (PreparedStatement statement = connection.prepareStatement(
                keywordQueries.getDeleteByBookIdQuery())) {
            statement.setLong(1, bookId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Error deleting keywords by book id: {}", ex.getMessage());
            throw new DaoException("Error deleting keywords by book id: " + ex.getMessage(), ex);
        }
    }
}
