package ua.org.training.library.dao.impl;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.postgres_queries.BookQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BookDaoImpl implements BookDao {
    private final BookQueries bookQueries;
    private final Collector<Book> collector;

    @Autowired
    public BookDaoImpl(BookQueries bookQueries,
                       @Qualifier("bookCollector") Collector<Book> collector) {
        this.bookQueries = bookQueries;
        this.collector = collector;
    }

    @Override
    public Page<Book> getBooksByAuthorId(Connection connection, Pageable page, Long authorId) {
        log.info("Getting books by author id: {}", authorId);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getBooksByAuthorIdQuery(page))) {
            statement.setLong(1, authorId);
            statement.setInt(2, page.getPageSize());
            statement.setLong(3, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                return new PageImpl<>(collector.collectList(resultSet), page, count(connection));
            }
        } catch (SQLException e) {
            log.error("Error getting books by author id: {}", authorId, e);
            throw new DaoException("Error getting books by author id: " + authorId, e);
        }
    }

    @Override
    public Page<Book> getBooksByLanguage(Connection connection, Pageable page, String language) {
        log.info("Getting books by language: {}", language);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getBooksByLanguageQuery(page))) {
            statement.setString(1, language);
            statement.setInt(2, page.getPageSize());
            statement.setLong(3, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                return new PageImpl<>(collector.collectList(resultSet), page, count(connection));
            }
        } catch (SQLException e) {
            log.error("Error getting books by language: {}", language, e);
            throw new DaoException("Error getting books by language: " + language, e);
        }
    }

    @Override
    public Optional<Book> getBookByOrderId(Connection connection, Long orderId) {
        log.info("Getting book by order id: {}", orderId);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getBookByOrderIdQuery())) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(collector.collect(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Error getting book by order id: {}", orderId, e);
            throw new DaoException("Error getting book by order id: " + orderId, e);
        }
    }

    @Override
    public Page<Book> getBooksWhichUserDidNotOrder(Connection connection, Pageable page, Long userId) {
        log.info("Getting books which user did not order: {}", userId);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getBooksWhichUserDidNotOrderQuery(page))) {
            statement.setLong(1, userId);
            statement.setInt(2, page.getPageSize());
            statement.setLong(3, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                return new PageImpl<>(collector.collectList(resultSet), page, countBooksWhichUserDidNotOrder(connection, userId));
            }
        } catch (SQLException e) {
            log.error("Error getting books which user did not order: {}", userId, e);
            throw new DaoException("Error getting books which user did not order: " + userId, e);
        }
    }

    private long countBooksWhichUserDidNotOrder(Connection connection, Long userId) {
        log.info("Counting books which user did not order: {}", userId);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getCountBooksWhichUserDidNotOrderQueryNoSearch())) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting books which user did not order: {}", userId, e);
            throw new DaoException("Error counting books which user did not order: " + userId, e);
        }
    }

    @Override
    public Page<Book> searchBooksWhichUserDidNotOrder(Connection connection, Pageable page, Long userId, String search) {
        log.info("Searching books which user did not order: {}, {}", userId, search);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getSearchBooksWhichUserDidNotOrderQuery(page))) {
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, "%" + search + "%");
            statement.setString(4, "%" + search + "%");
            statement.setString(5, "%" + search + "%");
            statement.setString(6, "%" + search + "%");
            statement.setLong(7, userId);
            statement.setInt(8, page.getPageSize());
            statement.setLong(9, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                return new PageImpl<>(collector.collectList(resultSet), page, countBooksWhichUserDidNotOrder(connection, userId, search));
            }
        } catch (SQLException e) {
            log.error("Error searching books which user did not order: {}, {}", userId, search, e);
            throw new DaoException("Error searching books which user did not order: " + userId + ", " + search, e);
        }
    }

    private long countBooksWhichUserDidNotOrder(Connection connection, Long userId, String search) {
        log.info("Counting books which user did not order: {}, {}", userId, search);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getCountBooksWhichUserDidNotOrderQuery())) {
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, "%" + search + "%");
            statement.setString(4, "%" + search + "%");
            statement.setString(5, "%" + search + "%");
            statement.setString(6, "%" + search + "%");
            statement.setLong(7, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error counting books which user did not order: {}, {}", userId, search, e);
            throw new DaoException("Error counting books which user did not order: " + userId + ", " + search, e);
        }
    }

    @Override
    public Optional<Book> getBookByISBN(Connection connection, String isbn) {
        log.info("Getting book by ISBN: {}", isbn);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getBookByISBNQuery())) {
            statement.setString(1, isbn);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(collector.collect(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error getting book by ISBN: {}", isbn, e);
            throw new DaoException("Error getting book by ISBN: " + isbn, e);
        }
    }

    @Override
    public Page<Book> searchBooks(Connection connection, Pageable page, String search) {
        log.info("Searching books: {}", search);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getSearchBooksQuery(page))) {
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, "%" + search + "%");
            statement.setString(4, "%" + search + "%");
            statement.setString(5, "%" + search + "%");
            statement.setString(6, "%" + search + "%");
            statement.setInt(7, page.getPageSize());
            statement.setLong(8, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                return new PageImpl<>(collector.collectList(resultSet), page, countSearch(connection, search));
            }
        } catch (SQLException e) {
            log.error("Error searching books: {}", search, e);
            throw new DaoException("Error searching books: " + search, e);
        }
    }

    @Override
    public long getBookCount(Connection conn, Long id) {
        log.info("Getting book count: {}", id);
        try (PreparedStatement statement = conn.prepareStatement(
                bookQueries.getBookCountQuery())) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            log.error("Error getting book count: {}", id, e);
            throw new DaoException("Error getting book count: " + id, e);
        }
    }

    @Override
    public List<Book> getBooksByAuthorId(Connection conn, Long id) {
        log.info("Getting books by author id: {}", id);
        try (PreparedStatement statement = conn.prepareStatement(
                bookQueries.getBooksByAuthorIdQuery())) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return collector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting books by author id: {}", id, e);
            throw new DaoException("Error getting books by author id: " + id, e);
        }
    }

    @Override
    public boolean existsByIsbn(Connection conn, String isbn) {
        log.info("Checking if book exists by ISBN: {}", isbn);
        try (PreparedStatement statement = conn.prepareStatement(
                bookQueries.getExistsByIsbnQuery())) {
            statement.setString(1, isbn);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            log.error("Error checking if book exists by ISBN: {}", isbn, e);
            throw new DaoException("Error checking if book exists by ISBN: " + isbn, e);
        }
    }

    private long countSearch(Connection connection, String search) {
        log.info("Counting books by search: {}", search);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getCountSearchBooksQuery())) {
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, "%" + search + "%");
            statement.setString(4, "%" + search + "%");
            statement.setString(5, "%" + search + "%");
            statement.setString(6, "%" + search + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            log.error("Error counting books by search: {}", search, e);
            throw new DaoException("Error counting books by search: " + search, e);
        }
    }

    @Override
    public Book create(Connection connection, Book model) {
        log.info("Creating book: {}", model);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getCreateBookQuery(), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getTitle());
            statement.setString(2, model.getDescription());
            statement.setString(3, model.getIsbn());
            statement.setLong(4, model.getCount());
            statement.setDate(5, Date.valueOf(model.getPublicationDate()));
            statement.setDouble(6, model.getFine());
            statement.setString(7, model.getLanguage());
            statement.setString(8, model.getLocation());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setId(rs.getLong(1));
                    return model;
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            log.error("Error creating book: {}", model, e);
            throw new DaoException("Error creating book: " + model, e);
        }
    }

    @Override
    public List<Book> create(Connection connection, List<Book> models) {
        log.info("Creating books: {}", models);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getCreateBookQuery())) {
            for (Book book : models) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getDescription());
                statement.setString(3, book.getIsbn());
                statement.setLong(4, book.getCount());
                statement.setDate(5, Date.valueOf(book.getPublicationDate()));
                statement.setDouble(6, book.getFine());
                statement.setString(7, book.getLanguage());
                statement.setString(8, book.getLocation());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                for (Book book : models) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating book failed, no ID obtained.");
                    }
                }
            }
            return models;
        } catch (SQLException e) {
            log.error("Error creating books: {}", models, e);
            throw new DaoException("Error creating books: " + models, e);
        }
    }

    @Override
    public Optional<Book> getById(Connection connection, long id) {
        log.info("Getting book by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getGetBookByIdQuery())) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(collector.collect(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error getting book by id: {}", id, e);
            throw new DaoException("Error getting book by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> getByIds(Connection connection, List<Long> ids) {
        log.info("Getting books by ids: {}", ids);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getGetBooksByIdsQuery(ids.size()))) {
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return collector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting books by ids: {}", ids, e);
            throw new DaoException("Error getting books by ids: " + ids, e);
        }
    }

    @Override
    public List<Book> getAll(Connection connection) {
        log.info("Getting all books");
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getGetAllBooksQuery())) {
            try (ResultSet resultSet = statement.executeQuery()) {
                return collector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all books", e);
            throw new DaoException("Error getting all books", e);
        }
    }

    @Override
    public List<Book> getAll(Connection connection, Sort sort) {
        log.info("Getting all books with sort: {}", sort);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getGetAllBooksQuery(sort))) {
            try (ResultSet resultSet = statement.executeQuery()) {
                return collector.collectList(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error getting all books with sort: {}", sort, e);
            throw new DaoException("Error getting all books with sort: " + sort, e);
        }
    }

    @Override
    public Page<Book> getPage(Connection connection, Pageable page) {
        log.info("Getting page of books: {}", page);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getGetPageOfBooksQuery(page))) {
            statement.setInt(1, page.getPageSize());
            statement.setLong(2, page.getOffset());
            try (ResultSet resultSet = statement.executeQuery()) {
                return new PageImpl<>(collector.collectList(resultSet), page, count(connection));
            }
        } catch (SQLException e) {
            log.error("Error getting page of books: {}", page, e);
            throw new DaoException("Error getting page of books: " + page, e);
        }
    }

    @Override
    public void update(Connection connection, Book entity) {
        log.info("Updating book: {}", entity);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getUpdateBookQuery())) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setString(3, entity.getIsbn());
            statement.setLong(4, entity.getCount());
            statement.setDate(5, Date.valueOf(entity.getPublicationDate()));
            statement.setDouble(6, entity.getFine());
            statement.setString(7, entity.getLanguage());
            statement.setString(8, entity.getLocation());
            statement.setLong(9, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating book: {}", entity, e);
            throw new DaoException("Error updating book: " + entity, e);
        }
    }

    @Override
    public void delete(Connection connection, long id) {
        log.info("Deleting book by id: {}", id);
        try (PreparedStatement statement = connection.prepareStatement(
                bookQueries.getDeleteBookByIdQuery())) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting book by id: {}", id, e);
            throw new DaoException("Error deleting book by id: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) {
        log.info("Counting books");
        try (PreparedStatement statement = conn.prepareStatement(
                bookQueries.getGetCountOfBooksQuery())) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting books", e);
            throw new DaoException("Error counting books", e);
        }
        return 0;
    }

    @Override
    public void deleteAll(Connection conn) {
        log.info("Deleting all books");
        try (PreparedStatement statement = conn.prepareStatement(
                bookQueries.getDeleteAllBooksQuery())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting all books", e);
            throw new DaoException("Error deleting all books", e);
        }
    }

    @Override
    public void deleteAll(Connection conn, List<? extends Long> longs) {
        log.info("Deleting books by ids: {}", longs);
        try (PreparedStatement statement = conn.prepareStatement(
                bookQueries.getDeleteBooksByIdsQuery(longs.size()))) {
            for (int i = 0; i < longs.size(); i++) {
                statement.setLong(i + 1, longs.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting books by ids: {}", longs, e);
            throw new DaoException("Error deleting books by ids: " + longs, e);
        }
    }

    @Override
    public void update(Connection conn, List<Book> entities) {
        log.info("Updating books: {}", entities);
        try (PreparedStatement statement = conn.prepareStatement(
                bookQueries.getUpdateBookQuery())) {
            for (Book book : entities) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getDescription());
                statement.setString(3, book.getIsbn());
                statement.setLong(4, book.getCount());
                statement.setDate(5, Date.valueOf(book.getPublicationDate()));
                statement.setDouble(6, book.getFine());
                statement.setString(7, book.getLanguage());
                statement.setString(8, book.getLocation());
                statement.setLong(9, book.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error("Error updating books: {}", entities, e);
            throw new DaoException("Error updating books: " + entities, e);
        }
    }
}
