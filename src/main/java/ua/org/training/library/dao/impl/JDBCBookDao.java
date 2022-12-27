package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.collector.BookCollector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JDBCBookDao implements BookDao {
    //language=MySQL
    private static final String TOTAL_BOOKS_COUNT = "SELECT COUNT(*) FROM books_catalog";
    //language=MySQL
    private static final String PAGE_GET_BOOKS_BY_AUTHOR_ID = "CALL PAGE_GET_BOOKS_BY_AUTHOR_ID(?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String PAGE_GET_BOOKS_BY_AUTHOR_ID_AND_LANGUAGE =
            "CALL PAGE_GET_BOOKS_BY_AUTHOR_ID_AND_LANGUAGE(?, ?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String PAGE_GET_BOOKS_BY_LANGUAGE = "CALL PAGE_GET_BOOKS_BY_LANGUAGE(?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String INSERT_BOOK = "INSERT INTO books_catalog (book_name, book_count, ISBN, " +
            "book_date_publication, fine_per_day, `language`) VALUES (?, ?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String GET_BOOK_BY_ID = "SELECT * FROM books_catalog WHERE book_id = ?";
    //language=MySQL
    private static final String PAGE_GET_BOOKS = "CALL PAGE_GET_BOOKS(?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String DELETE_BOOK_AUTHOR = "DELETE FROM author_book WHERE book_id = ?";
    //language=MySQL
    private static final String DELETE_BOOK = "DELETE FROM books_catalog WHERE book_id = ?";
    //language=MySQL
    private static final String UPDATE_BOOK = "UPDATE books_catalog SET book_name = ?, book_count = ?, ISBN = ?, " +
            "book_date_publication = ?, fine_per_day = ?, `language` = ? WHERE book_id = ?";
    //language=MySQL
    private static final String DELETE_BOOK_AUTHOR_BY_AUTHOR_ID = "DELETE FROM author_book WHERE author_id = ?";
    //language=MySQL
    private static final String BOOK_BY_ORDER_ID = "SELECT b.* from books_catalog b " +
            "JOIN order_list o ON b.book_id = o.book_id WHERE o.id = ?";
    //language=MySQL
    private static final String PAGE_GET_BOOKS_WHICH_USER_DID_NOT_ORDER =
            "CALL PAGE_GET_BOOKS_NOT_ORDERED(?, ?, ?, ?, ?, ?)";
    //language=MySQL
    private static final String COUNT_BOOKS_WHICH_USER_DID_NOT_ORDER =
            "SELECT COUNT(*)  from books_catalog b where b.book_id " +
                    "NOT IN(" +
                    "SELECT bc.book_id FROM order_list ol " +
                    "inner join books_catalog bc on ol.book_id=bc.book_id " +
                    "where ol.user_id = ?) ";
    //language=MySQL
    private static final String INSERT_BOOK_AUTHOR = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
    private static final Logger LOGGER = LogManager.getLogger(JDBCBookDao.class);
    private final Connection connection;
    public JDBCBookDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Page<Book> getBooksByAuthorId(Page<Book> page, Long authorId) {
        List<Book> books = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall(PAGE_GET_BOOKS_BY_AUTHOR_ID)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setLong(3, authorId);
            getUniqueBooksSortedBy(page, books, statement);
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get books by author id: %s", authorId), e);
            throw new DaoException("Cannot get books by author id: " + authorId, e);
        }
        return page;
    }

    @Override
    public Page<Book> getBooksByAuthorIdAndLanguage(Page<Book> page, Long authorId, String language) {
        try (CallableStatement statement = connection.prepareCall(PAGE_GET_BOOKS_BY_AUTHOR_ID_AND_LANGUAGE)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setLong(3, authorId);
            statement.setString(4, language);
            statement.setString(5, page.getSearch());
            statement.setString(6, page.getSorting());
            List<Book> books;
            try (ResultSet resultSet = statement.executeQuery()) {
                books = new ArrayList<>();
                BookCollector bookCollector = new BookCollector();
                while (resultSet.next()) {
                    books.add(bookCollector.collectFromResultSet(resultSet));
                }
                page.setData(books);
                page.setElementsCount(books.size());
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("Cannot get books by author id: %s and language: %s", authorId, language), e);
            throw new DaoException("Cannot get books by author id: " + authorId + " and language: " + language, e);
        }
        return page;
    }

    @Override
    public Page<Book> getBooksByLanguage(Page<Book> page, String language) {
        List<Book> books = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall(PAGE_GET_BOOKS_BY_LANGUAGE)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, language);
            getUniqueBooksSortedBy(page, books, statement);
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get books by language: %s", language), e);
            throw new DaoException("Cannot get books by language: " + language, e);
        }
        return page;
    }

    @Override
    public Page<Book> getBooksSortedBy(Page<Book> page, String orderBy) {
        List<Book> books = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall(PAGE_GET_BOOKS)){
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, orderBy);
            getUniqueBooksSortedBy(page, books, statement);
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get books sorted by: %s", orderBy), e);
            throw new DaoException("Cannot get books sorted by: " + orderBy, e);
        }
        return page;
    }

    @Override
    public Optional<Book> getBookByOrderId(Long orderId) {
        try (PreparedStatement statement = connection.prepareStatement(BOOK_BY_ORDER_ID)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                BookCollector bookCollector = new BookCollector();
                if (resultSet.next()) {
                    return Optional.ofNullable(bookCollector.collectFromResultSet(resultSet));
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get book by order id: %s", orderId), e);
            throw new DaoException("Cannot get book by order id: " + orderId, e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Book> getBooksWhichUserDidNotOrder(Page<Book> page, Long userId, String orderBy) {
        List<Book> books = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall(PAGE_GET_BOOKS_WHICH_USER_DID_NOT_ORDER)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, orderBy);
            statement.setLong(4, userId);
            statement.setString(5, page.getSearch());
            statement.setString(6, page.getSorting());
            try (ResultSet resultSet = statement.executeQuery()) {
                BookCollector bookCollector = new BookCollector();
                while (resultSet.next()) {
                    books.add(bookCollector.collectFromResultSet(resultSet));
                }
                page.setData(books);
                try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_BOOKS_WHICH_USER_DID_NOT_ORDER)) {
                    preparedStatement.setLong(1, userId);
                    try (ResultSet resultSet1 = preparedStatement.executeQuery()) {
                        if (resultSet1.next()) {
                            page.setElementsCount(resultSet1.getInt(1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get books which user did not order: %s", userId), e);
            throw new DaoException("Cannot get books which user did not order: " + userId, e);
        }
        return page;
    }

    @Override
    public long create(Book model) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            setBookDataInPreparedStatement(model, statement);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long bookId = resultSet.getLong(1);
                    insertAuthors(model.getAuthors(), bookId);
                    connection.commit();
                    return bookId;
                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error(String.format("Cannot create book: %s", model), e);
            throw new DaoException("Cannot create book: " + model, e);
        } finally {
            connection.setAutoCommit(true);
        }
        return Constants.APP_DEFAULT_ID;
    }

    private void insertAuthors(Collection<Author> authors, long aLong) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK_AUTHOR)) {
            for (Author author : authors) {
                statement.setLong(1, author.getId());
                statement.setLong(2, aLong);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot insert authors: %s", authors), e);
            throw new DaoException("Cannot insert authors: " + authors, e);
        }
    }

    @Override
    public Optional<Book> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_BOOK_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                BookCollector bookCollector = new BookCollector();
                if (resultSet.next()) {
                    return Optional.of(bookCollector.collectFromResultSet(resultSet));
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot get book by id: %s", id), e);
            throw new DaoException("Cannot get book by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Book> getPage(Page<Book> page) {
        List<Book> books = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall(PAGE_GET_BOOKS)) {
            statement.setLong(1, page.getLimit());
            statement.setLong(2, page.getOffset());
            statement.setString(3, "book_name");
            getUniqueBooksSortedBy(page, books, statement);
        } catch (Exception e) {
            LOGGER.error("Cannot get books", e);
            throw new DaoException("Cannot get books", e);
        }
        return page;
    }

    private void getUniqueBooksSortedBy(Page<Book> page, List<Book> books, CallableStatement statement) throws SQLException {
        statement.setString(4, page.getSearch());
        statement.setString(5, page.getSorting());
        try (ResultSet resultSet = statement.executeQuery()) {
            BookCollector bookCollector = new BookCollector();
            while (resultSet.next()) {
                books.add(bookCollector.collectFromResultSet(resultSet));
            }
        }
        page.setData(books);
        try (PreparedStatement preparedStatement = connection.prepareStatement(TOTAL_BOOKS_COUNT)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    page.setElementsCount(resultSet.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Book entity) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK)) {
            setBookDataInPreparedStatement(entity, statement);
            statement.setLong(7, entity.getId());
            statement.executeUpdate();
            deleteAuthors(entity.getId());
            insertAuthors(entity.getAuthors(), entity.getId());
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            LOGGER.error(String.format("Cannot update book: %s", entity), e);
            throw new DaoException("Cannot update book: " + entity, e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void deleteAuthors(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BOOK_AUTHOR)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot delete authors: %s", id), e);
            throw new DaoException("Cannot delete authors: " + id, e);
        }
    }

    private void setBookDataInPreparedStatement(Book entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setInt(2, entity.getCount());
        statement.setString(3, entity.getISBN());
        statement.setTimestamp(4, new Timestamp(entity.getPublicationDate().getTime()));
        statement.setDouble(5, entity.getFine());
        statement.setString(6, entity.getLanguage());
    }

    @Override
    public void delete(long id) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BOOK_AUTHOR)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            try (PreparedStatement statement1 = connection.prepareStatement(DELETE_BOOK)) {
                statement1.setLong(1, id);
                statement1.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error(String.format("Cannot delete book by id: %s", id), e);
            throw new DaoException("Cannot delete book by id: " + id, e);
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
