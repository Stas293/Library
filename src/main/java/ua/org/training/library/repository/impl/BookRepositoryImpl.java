package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.KeywordDao;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.BookRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookRepositoryImpl implements BookRepository {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final KeywordDao keywordDao;
    private final TransactionManager transactionManager;

    @Override
    public Book save(Book entity) {
        log.info("Saving book: {}", entity);
        Connection conn = transactionManager.getConnection();
        if (entity.getId() == null) {
            log.info("Creating book: {}", entity);
            entity = bookDao.create(conn, entity);
        } else {
            log.info("Updating book: {}", entity);
            bookDao.update(conn, entity);
        }
        log.info("Setting authors and keywords to book: {}", entity);
        authorDao.setAuthorsToBook(conn, entity.getId(), entity.getAuthors());
        keywordDao.setKeywordsToBook(conn, entity.getId(), entity.getKeywords());
        return entity;
    }

    @Override
    public List<Book> saveAll(List<Book> entities) {
        log.info("Saving books: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Book> booksToSave = entities.stream()
                .filter(book -> book.getId() == null)
                .toList();
        List<Book> booksToUpdate = entities.stream()
                .filter(book -> book.getId() != null)
                .toList();
        log.info("Creating books: {}", booksToSave);
        if (!booksToSave.isEmpty()) {
            booksToSave = bookDao.create(conn, booksToSave);
        }
        log.info("Updating books: {}", booksToUpdate);
        if (!booksToUpdate.isEmpty()) {
            bookDao.update(conn, booksToUpdate);
        }
        booksToSave.addAll(booksToUpdate);
        log.info("Setting authors and keywords to books: {}", booksToSave);
        for (Book book : booksToSave) {
            authorDao.setAuthorsToBook(conn, book.getId(), book.getAuthors());
            keywordDao.setKeywordsToBook(conn, book.getId(), book.getKeywords());
        }
        return booksToSave;
    }

    @Override
    public Optional<Book> findById(Long aLong) {
        log.info("Getting book by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        Book book = bookDao.getById(conn, aLong).orElse(null);
        log.info("Setting authors and keywords to book: {}", book);
        if (book != null) {
            book.setAuthors(authorDao.getAuthorsByBookId(conn, book.getId()));
            book.setKeywords(keywordDao.getKeywordsByBookId(conn, book.getId()));
        }
        return Optional.ofNullable(book);
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if book exists by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        return bookDao.getById(conn, aLong).isPresent();
    }

    @Override
    public List<Book> findAll() {
        log.info("Getting all books");
        Connection conn = transactionManager.getConnection();
        return bookDao.getAll(conn);
    }

    @Override
    public List<Book> findAllById(List<Long> longs) {
        log.info("Getting books by ids: {}", longs);
        Connection conn = transactionManager.getConnection();
        return bookDao.getByIds(conn, longs);
    }

    @Override
    public long count() {
        log.info("Counting books");
        Connection conn = transactionManager.getConnection();
        return bookDao.count(conn);
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting book by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        bookDao.delete(conn, aLong);
    }

    @Override
    public void delete(Book entity) {
        log.info("Deleting book: {}", entity);
        Connection conn = transactionManager.getConnection();
        bookDao.delete(conn, entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting books by ids: {}", ids);
        Connection conn = transactionManager.getConnection();
        bookDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll(List<? extends Book> entities) {
        log.info("Deleting books: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Long> ids = entities.stream().map(Book::getId).toList();
        log.info("Deleting books by ids: {}", ids);
        bookDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all books");
        Connection conn = transactionManager.getConnection();
        bookDao.deleteAll(conn);
    }

    @Override
    public List<Book> findAll(Sort sort) {
        log.info("Getting all books sorted by: {}", sort);
        Connection conn = transactionManager.getConnection();
        return bookDao.getAll(conn, sort);
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        log.info("Getting all books by page: {}", pageable);
        Connection conn = transactionManager.getConnection();
        return bookDao.getPage(conn, pageable);
    }

    @Override
    public Page<Book> getBooksByAuthor(Pageable page, Author author) {
        log.info("Getting books by author: {}", author);
        Connection conn = transactionManager.getConnection();
        Page<Book> books = bookDao.getBooksByAuthorId(conn, page, author.getId());
        log.info("Setting keywords to books: {}", books);
        for (Book book : books.getContent()) {
            book.setKeywords(keywordDao.getKeywordsByBookId(conn, book.getId()));
        }
        return books;
    }

    @Override
    public Page<Book> getBooksByLanguage(Pageable page, Locale language) {
        log.info("Getting books by language: {}", language);
        Connection conn = transactionManager.getConnection();
        return bookDao.getBooksByLanguage(conn, page, language.getLanguage());
    }

    @Override
    public Optional<Book> getBookByOrder(Order order) {
        log.info("Getting book by order: {}", order);
        Connection conn = transactionManager.getConnection();
        Optional<Book> book = bookDao.getBookByOrderId(conn, order.getId());
        log.info("Setting authors and keywords to book: {}", book);
        book.ifPresent(value -> {
            value.setAuthors(authorDao.getAuthorsByBookId(conn, value.getId()));
            value.setKeywords(keywordDao.getKeywordsByBookId(conn, value.getId()));
        });
        return book;
    }

    @Override
    public Page<Book> getBooksExceptUserOrders(Pageable page, User user) {
        log.info("Getting books except user orders: {}", user);
        Connection conn = transactionManager.getConnection();
        return bookDao.getBooksWhichUserDidNotOrder(
                conn, page, user.getId());
    }

    @Override
    public Page<Book> searchBooksExceptUserOrders(Pageable page, User user, String search) {
        log.info("Searching books except user orders: {}", user);
        Connection conn = transactionManager.getConnection();
        return bookDao.searchBooksWhichUserDidNotOrder(
                conn, page, user.getId(), search);
    }

    @Override
    public Optional<Book> getBookByISBN(String isbn) {
        log.info("Getting book by ISBN: {}", isbn);
        Connection conn = transactionManager.getConnection();
        Optional<Book> book = bookDao.getBookByISBN(conn, isbn);
        log.info("Setting authors and keywords to book: {}", book);
        book.ifPresent(value -> {
            value.setAuthors(authorDao.getAuthorsByBookId(conn, value.getId()));
            value.setKeywords(keywordDao.getKeywordsByBookId(conn, value.getId()));
        });
        return book;
    }

    @Override
    public Page<Book> searchBooks(Pageable page, String search) {
        log.info("Searching books by page: {}", page);
        Connection connection = transactionManager.getConnection();
        return bookDao.searchBooks(connection, page, search);
    }
}
