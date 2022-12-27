package ua.org.training.library.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JDBCBookDaoTest {
    private static BookDao bookDao;
    private static final Logger LOGGER = LogManager.getLogger(JDBCBookDaoTest.class);
    @BeforeEach
    void setUp() {
        try {
            bookDao = DaoFactory.getInstance().createBookDao();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
    }
    @Test
    void getBooksByAuthorId() {
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .createPage();
        Page<Book> correct = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setISBN("9780747532743")
                                .setName("Harry Potter and the Philosopher's Stone")
                                .setCount(3)
                                .setPublicationDate(new Date(115, Calendar.JULY, 21))
                                .setLanguage("en")
                                .setFine(200.0)
                                .setAuthors(null)
                                .createBook()))
                .createPage();
        LOGGER.info(page);
        assertEquals(correct, bookDao.getBooksByAuthorId(page, 1L));
    }

    @Test
    void getBooksByAuthorIdAndLanguage() {
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .createPage();
        Page<Book> correct = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setISBN("9780747532743")
                                .setName("Harry Potter and the Philosopher's Stone")
                                .setCount(3)
                                .setPublicationDate(new Date(115, Calendar.JULY, 21))
                                .setLanguage("en")
                                .setFine(200.0)
                                .setAuthors(null)
                                .createBook()))
                .createPage();
        assertEquals(correct, bookDao.getBooksByAuthorIdAndLanguage(page, 1L, "en"));
    }

    @Test
    void getBooksByLanguage() {
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .createPage();
        Page<Book> correct = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setISBN("9780747532743")
                                .setName("Harry Potter and the Philosopher's Stone")
                                .setCount(3)
                                .setPublicationDate(new Date(115, Calendar.JULY, 21))
                                .setLanguage("en")
                                .setFine(200.0)
                                .setAuthors(null)
                                .createBook()))
                .createPage();
        assertEquals(correct, bookDao.getBooksByLanguage(page, "en"));
    }

    @Test
    void getBooksSortedBy() {
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .createPage();
        Page<Book> correct = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("Harry")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setISBN("9780747532743")
                                .setName("Harry Potter and the Philosopher's Stone")
                                .setCount(3)
                                .setPublicationDate(new Date(115, Calendar.JULY, 21))
                                .setLanguage("en")
                                .setFine(200.0)
                                .setAuthors(null)
                                .createBook()))
                .createPage();
        assertEquals(correct, bookDao.getBooksSortedBy(page, "last_name, first_name"));
    }

    @Test
    void create_delete() throws SQLException, JDBCException {
        Book book = Book.builder()
                .setISBN("123456789")
                .setName("Test book")
                .setCount(1)
                .setPublicationDate(new Date(120, Calendar.JANUARY, 1))
                .setLanguage("en")
                .setFine(100.0)
                .setAuthors(null)
                .createBook();
        book.setId(bookDao.create(book));
        bookDao = DaoFactory.getInstance().createBookDao();
        Optional<Book> optionalBook = bookDao.getById(book.getId());
        assertTrue(optionalBook.isPresent());
        bookDao = DaoFactory.getInstance().createBookDao();
        assertEquals(book, optionalBook.get());
        bookDao = DaoFactory.getInstance().createBookDao();
        bookDao.delete(book.getId());
    }

    @Test
    void getById() throws JDBCException {
        Optional<Book> correct = Optional.of(Book.builder()
                .setId(1L)
                .setISBN("9780747532743")
                .setName("Harry Potter and the Philosopher's Stone")
                .setCount(3)
                .setPublicationDate(new Date(115, Calendar.JULY, 21))
                .setLanguage("en")
                .setFine(200.0)
                .setAuthors(null)
                .createBook());
        assertEquals(correct, bookDao.getById(1L));
        bookDao = DaoFactory.getInstance().createBookDao();
        assertEquals(Optional.empty(), bookDao.getById(100L));
    }

    @Test
    void getPage() {
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(3)
                .setSorting("ASC")
                .createPage();
        Page<Book> correct = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(3)
                .setSorting("ASC")
                .setElementsCount(3)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setISBN("9780747532743")
                                .setName("Harry Potter and the Philosopher's Stone")
                                .setCount(3)
                                .setPublicationDate(new Date(115, Calendar.JULY, 21))
                                .setLanguage("en")
                                .setFine(200.0)
                                .setAuthors(null)
                                .createBook(),
                        Book.builder()
                                .setId(3L)
                                .setISBN("9780316430913")
                                .setName("My dad had that car : a nostalgic look at the American automobile")
                                .setCount(5)
                                .setPublicationDate(new Date(117, Calendar.MAY, 9))
                                .setLanguage("en")
                                .setFine(50.0)
                                .setAuthors(null)
                                .createBook(),
                        Book.builder()
                                .setId(2L)
                                .setISBN("9781946433336")
                                .setName("Story")
                                .setCount(2)
                                .setPublicationDate(new Date(119, Calendar.DECEMBER, 1))
                                .setLanguage("en")
                                .setFine(100.0)
                                .setAuthors(null)
                                .createBook()))
                .createPage();
        assertEquals(correct, bookDao.getPage(page));
    }

    @Test
    void update() throws SQLException, JDBCException {
        Book book = Book.builder()
                .setId(1L)
                .setISBN("9780747532743")
                .setName("Harry Potter and the Philosopher's Stone")
                .setCount(3)
                .setPublicationDate(new Date(115, Calendar.JULY, 21))
                .setLanguage("en")
                .setFine(200.0)
                .setAuthors(null)
                .createBook();
        bookDao.update(book);
        bookDao = DaoFactory.getInstance().createBookDao();
        assertEquals(Optional.of(book), bookDao.getById(1L));
    }
}