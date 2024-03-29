package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.enums.constants.AuthorQueries;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Author;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AuthorDaoImpl implements AuthorDao {
    private final AuthorQueries authorQueries;
    private final Collector<Author> authorCollector;

    @Autowired
    public AuthorDaoImpl(AuthorQueries authorQueries,
                         @Qualifier("authorCollector") Collector<Author> authorCollector) {
        this.authorQueries = authorQueries;
        this.authorCollector = authorCollector;
    }

    @Override
    public List<Author> getAuthorsByBookId(Connection connection, Long id) {
        log.info("Get authors by book id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getAuthorsByBookId())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return authorCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error(String.format("Cannot get authors by book id: %s", id), e);
            throw new DaoException("Cannot get authors by book id: " + id, e);
        }
    }

    @Override
    public void setAuthorsToBook(Connection connection, Long bookId, List<Author> authors) {
        log.info("Set authors to book: {}", bookId);
        deleteAuthorsByBookId(connection, bookId);
        saveAuthorsToBook(connection, bookId, authors);
    }

    @Override
    public Page<Author> searchAuthors(Connection conn, Pageable page, String search) {
        log.info("Search authors: {}", search);
        try (PreparedStatement statement = conn.prepareStatement(
                authorQueries.getSearchAuthors(page.getSort()),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(page.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            String searchParam = "%" + search + "%";
            statement.setString(1, searchParam);
            statement.setString(2, searchParam);
            statement.setString(3, searchParam);
            statement.setInt(4, page.getPageSize());
            statement.setLong(5, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Author> authors = authorCollector.collectList(resultSet);
                resultSet.last();
                long total = resultSet.getLong(5);
                return new PageImpl<>(authors, page, total);
            }
        } catch (SQLException e) {
            log.error(String.format("Cannot search authors: %s", search), e);
            throw new DaoException("Cannot search authors: " + search, e);
        }
    }

    @Override
    public List<Author> findAllByNameContainingIgnoreCase(Connection conn, String search) {
        log.info("Find all authors by name containing: {}", search);
        try (PreparedStatement statement = conn.prepareStatement(
                authorQueries.getFindAllByNameContainingIgnoreCase())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            String searchParam = "%" + search + "%";
            statement.setString(1, searchParam);
            statement.setString(2, searchParam);
            statement.setString(3, searchParam);
            try (ResultSet resultSet = statement.executeQuery()) {
                return authorCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error(String.format("Cannot find all authors by name containing: %s", search), e);
            throw new DaoException("Cannot find all authors by name containing: " + search, e);
        }
    }

    private void saveAuthorsToBook(Connection connection, Long bookId, List<Author> authors) {
        log.info("Save authors to book: {}", bookId);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getSaveAuthorsToBook())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (Author author : authors) {
                statement.setLong(1, bookId);
                statement.setLong(2, author.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error(String.format("Cannot save authors to book: %s", bookId), e);
            throw new DaoException("Cannot save authors to book: " + bookId, e);
        }
    }

    private void deleteAuthorsByBookId(Connection connection, Long bookId) {
        log.info("Delete authors by book id: {}", bookId);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getDeleteAuthorsByBookId())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(String.format("Cannot delete authors by book id: %s", bookId), e);
            throw new DaoException("Cannot delete authors by book id: " + bookId, e);
        }
    }

    @Override
    public Author create(Connection connection, Author model) {
        log.info("Create author: {}", model);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getCreateAuthor(),
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getFirstName());
            statement.setString(2, model.getMiddleName());
            statement.setString(3, model.getLastName());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getLong(1));
                    return model;
                } else {
                    throw new SQLException("Failed to create author, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            log.error(String.format("Cannot create author: %s", model), e);
            throw new DaoException("Cannot create author: " + model, e);
        }
    }

    @Override
    public List<Author> create(Connection connection, List<Author> models) {
        log.info("Create authors: {}", models);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getCreateAuthor(),
                Statement.RETURN_GENERATED_KEYS)) {
            for (Author model : models) {
                statement.setString(1, model.getFirstName());
                statement.setString(2, model.getMiddleName());
                statement.setString(3, model.getLastName());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                for (Author model : models) {
                    if (generatedKeys.next()) {
                        model.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Failed to create author, no ID obtained.");
                    }
                }
                return models;
            }
        } catch (SQLException e) {
            log.error(String.format("Cannot create authors: %s", models), e);
            throw new DaoException("Cannot create authors: " + models, e);
        }
    }

    @Override
    public Optional<Author> getById(Connection connection, long id) {
        log.info("Get author by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getAuthorById())) {
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(authorCollector.collect(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error(String.format("Cannot get author by id: %s", id), e);
            throw new DaoException("Cannot get author by id: " + id, e);
        }
    }

    @Override
    public List<Author> getByIds(Connection connection, List<Long> ids) {
        log.info("Get authors by ids: {}", ids);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getAuthorsByIds(ids.size()))) {
            statement.setFetchSize(ids.size());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return authorCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error(String.format("Cannot get authors by ids: %s", ids), e);
            throw new DaoException("Cannot get authors by ids: " + ids, e);
        }
    }

    @Override
    public List<Author> getAll(Connection connection) {
        log.info("Get all authors");
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getAllAuthors())) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet resultSet = statement.executeQuery()) {
                return authorCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Cannot get all authors", e);
            throw new DaoException("Cannot get all authors", e);
        }
    }

    @Override
    public List<Author> getAll(Connection connection, Sort sort) {
        log.info("Get all authors");
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getAllAuthors(sort))) {
            statement.setFetchSize(100);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet resultSet = statement.executeQuery()) {
                return authorCollector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Cannot get all authors", e);
            throw new DaoException("Cannot get all authors", e);
        }
    }

    @Override
    public Page<Author> getPage(Connection connection, Pageable page) {
        log.info("Get page of authors");
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getPageAuthors(page.getSort()),
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(page.getPageSize());
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Author> authors = authorCollector.collectList(resultSet);
                resultSet.last();
                long total = resultSet.getLong(5);
                return new PageImpl<>(authors, page, total);
            }
        } catch (SQLException e) {
            log.error("Cannot get page of authors", e);
            throw new DaoException("Cannot get page of authors", e);
        }
    }

    @Override
    public void update(Connection connection, Author entity) {
        log.info("Update author: {}", entity);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getUpdateAuthor())) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getMiddleName());
            statement.setString(3, entity.getLastName());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(String.format("Cannot update author: %s", entity), e);
            throw new DaoException("Cannot update author: " + entity, e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Delete author by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                authorQueries.getDeleteAuthor())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(String.format("Cannot delete author by id: %s", id), e);
            throw new DaoException("Cannot delete author by id: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Count authors");
        try (PreparedStatement statement = conn.prepareStatement(
                authorQueries.getCountAuthors())) {
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    throw new SQLException("Failed to count authors, no result obtained.");
                }
            }
        } catch (SQLException e) {
            log.error("Cannot count authors", e);
            throw new DaoException("Cannot count authors", e);
        }
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Delete all authors");
        try (PreparedStatement statement = conn.prepareStatement(
                authorQueries.getDeleteAllAuthors())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot delete all authors", e);
            throw new DaoException("Cannot delete all authors", e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Delete authors by ids: {}", longs);
        try (PreparedStatement statement = conn.prepareStatement(
                authorQueries.getDeleteAuthorsByIds(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                statement.setLong(i + 1, longs.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(String.format("Cannot delete authors by ids: %s", longs), e);
            throw new DaoException("Cannot delete authors by ids: " + longs, e);
        }
    }

    @Override
    public void update(Connection conn, List<Author> entities) {
        log.info("Update authors: {}", entities);
        try (PreparedStatement statement = conn.prepareStatement(
                authorQueries.getUpdateAuthor())) {
            for (Author entity : entities) {
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getMiddleName());
                statement.setString(3, entity.getLastName());
                statement.setLong(4, entity.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error(String.format("Cannot update authors: %s", entities), e);
            throw new DaoException("Cannot update authors: " + entities, e);
        }
    }
}
