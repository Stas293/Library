package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.collector.AuthorCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCAuthorDao implements AuthorDao {
    //language=MySQL
    private static final String GET_AUTHOR_BY_ID = "SELECT * FROM author_list WHERE id = ?";
    //language=MySQL
    private static final String GET_AUTHORS_BY_BOOK_ID =
            "SELECT author_list.* FROM author_list inner join " +
                    "author_book ab on author_list.id = ab.author_id " +
                    "where ab.book_id = ?";
    //language=MySQL
    private static final String GET_AUTHORS_BY_BOOK_ISBN =
            "SELECT author_list.* FROM author_list inner join " +
                    "author_book ab on author_list.id = ab.author_id " +
                    "inner join books_catalog bl on ab.book_id = bl.book_id " +
                    "where bl.isbn = ?";
    //language=MySQL
    private static final String GET_COUNT_OF_AUTHORS = "SELECT COUNT(*) FROM author_list";
    //language=MySQL
    private static final String GET_AUTHORS_PAGE = "CALL GET_AUTHORS_PAGE(?, ?, ?, ?);";
    //language=MySQL
    private static final String CREATE_AUTHOR = "INSERT INTO author_list (first_name, last_name) VALUES (?, ?)";
    //language=MySQL
    private static final String UPDATE_AUTHOR = "UPDATE author_list SET first_name = ?, last_name = ? WHERE id = ?";
    //language=MySQL
    private static final String DELETE_AUTHOR = "DELETE FROM author_list WHERE id = ?";
    //language=MySQL
    private static final String DELETE_AUTHOR_BOOK = "DELETE FROM author_book WHERE author_id = ?";
    //language=MySQL
    private static final String SET_BOOK_AUTHOR = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
    private static final Logger LOGGER = LogManager.getLogger(JDBCAuthorDao.class);
    private final Connection connection;

    public JDBCAuthorDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Author> getAuthorsByBookId(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_AUTHORS_BY_BOOK_ID)) {
            statement.setLong(1, id);
            return getAuthors(statement);
        } catch (SQLException e) {
            LOGGER.error(String.format("Cannot get authors by book id: %s", id), e);
            throw new DaoException("Cannot get authors by book id: " + id, e);
        }
    }

    private List<Author> getAuthors(PreparedStatement statement) throws SQLException {
        List<Author> authors;
        try (ResultSet resultSet = statement.executeQuery()) {
            authors = new ArrayList<>();
            AuthorCollector authorCollector = new AuthorCollector();
            while (resultSet.next()) {
                authors.add(authorCollector.collectFromResultSet(resultSet));
            }
        }
        return authors;
    }

    @Override
    public List<Author> getAuthorsByBookIsbn(String isbn) {
        try (PreparedStatement statement = connection.prepareStatement(GET_AUTHORS_BY_BOOK_ISBN)) {
            statement.setString(1, isbn);
            return getAuthors(statement);
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get authors by book isbn: %s", isbn), e);
            throw new DaoException("Cannot get authors by book isbn: " + isbn, e);
        }
    }

    @Override
    public long create(Author model) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(CREATE_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getFirstName());
            statement.setString(2, model.getLastName());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    connection.commit();
                    return id;
                }
            }
        } catch (Exception e) {
            connection.rollback();
            LOGGER.error(String.format("Cannot create author: %s", model), e);
            throw new DaoException("Cannot create author: " + model, e);
        } finally {
            connection.setAutoCommit(true);
        }
        return 0;
    }

    @Override
    public Optional<Author> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_AUTHOR_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                AuthorCollector authorCollector = new AuthorCollector();
                if (resultSet.next()) {
                    return Optional.of(authorCollector.collectFromResultSet(resultSet));
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get author by id: %s", id), e);
            throw new DaoException("Cannot get author by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Author> getPage(Page<Author> page) {
        try (CallableStatement statement = connection.prepareCall(GET_AUTHORS_PAGE)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, page.getSearch());
            statement.setString(4, page.getSorting());
            List<Author> authors;
            try (ResultSet resultSet = statement.executeQuery()) {
                authors = new ArrayList<>();
                AuthorCollector authorCollector = new AuthorCollector();
                while (resultSet.next()) {
                    authors.add(authorCollector.collectFromResultSet(resultSet));
                }
                page.setData(authors);
                try (Statement countStatement = connection.createStatement()) {
                    try (ResultSet countResultSet = countStatement.executeQuery(GET_COUNT_OF_AUTHORS)) {
                        if (countResultSet.next()) {
                            page.setElementsCount(countResultSet.getLong(1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get authors page: %s", page), e);
            throw new DaoException("Cannot get authors page: " + page, e);
        }
        return page;
    }

    @Override
    public void update(Author entity) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_AUTHOR)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            LOGGER.error(String.format("Cannot update author: %s", entity), e);
            throw new DaoException("Cannot update author: " + entity, e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_AUTHOR_BOOK)) {
            statement.setLong(1, id);
            try (PreparedStatement statement1 = connection.prepareStatement(DELETE_AUTHOR)) {
                statement1.setLong(1, id);
                statement1.executeUpdate();
            }
            statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            LOGGER.error(String.format("Cannot delete author by id: %s", id), e);
            throw new DaoException("Cannot delete author by id: " + id, e);
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
}
