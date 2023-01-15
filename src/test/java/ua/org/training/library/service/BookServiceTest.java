package ua.org.training.library.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private final long id = 0;
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    private BookService bookService;

    @Test
    void getBooksByAuthorId() throws JDBCException, ServiceException, ConnectionDBException {
        Page<Book> page = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .createPage();
        Date dateOfCreation = Utility.parseDateOrDefault("2020-01-01", new Date());
        Page<Book> resultBookPage = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setElementsCount(2)
                .setData(List.of(new Book(1L, "name", 5, "ISBN", dateOfCreation, 100.0, "en_US", null),
                        new Book(2L, "another book", 5, "new ISBN", dateOfCreation, 10.0, "en_US", null)))
                .createPage();
        Mockito.when(bookDao.getBooksByAuthorId(page, id)).thenReturn(resultBookPage);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(authorDao.getAuthorsByBookId(1L)).thenReturn(List.of(new Author(1L, "name", "surname")));
        Mockito.when(authorDao.getAuthorsByBookId(2L)).thenReturn(List.of(new Author(1L, "name", "surname")));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        bookService = new BookService(daoFactory);
        String result = bookService.getBooksByAuthorId(Locale.US, page, id);
        assertEquals(
                "{\"elementsCount\":2," +
                        "\"limit\":5," +
                        "\"content\"" +
                        ":[{\"id\":1,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2020-01-01\"," +
                        "\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":" +
                        "[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}," +
                        "{\"id\":2,\"name\":\"another book\",\"count\":5,\"publicationDate\":\"2020-01-01\"," +
                        "\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"new ISBN\"}]}",
                result);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        daoFactory = Mockito.mock(DaoFactory.class);
        assertThrows(ConnectionDBException.class, () -> bookService.getBooksByAuthorId(Locale.US, page, id));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.getBooksByAuthorId(Locale.US, page, id));
    }

    @Test
    void getBooksByLanguage() throws JDBCException, ServiceException, ConnectionDBException {
        Page<Book> page = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .createPage();
        Date dateOfCreation = Utility.parseDateOrDefault("2020-01-01", new Date());
        Page<Book> resultBookPage = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setElementsCount(3)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setName("name")
                                .setCount(5)
                                .setPublicationDate(dateOfCreation)
                                .setFine(100.0)
                                .setLanguage("en_US")
                                .setISBN("ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(2L)
                                .setName("another book")
                                .setCount(5)
                                .setPublicationDate(dateOfCreation)
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(3L)
                                .setName("another book1")
                                .setCount(5)
                                .setPublicationDate(dateOfCreation)
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN1")
                                .createBook(),
                        Book.builder()
                                .setId(4L)
                                .setName("another book2")
                                .setCount(5)
                                .setPublicationDate(dateOfCreation)
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN2")
                                .createBook()))
                .createPage();
        Mockito.when(bookDao.getBooksByLanguage(page, "en_US")).thenReturn(resultBookPage);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(authorDao.getAuthorsByBookId(1L)).thenReturn(List.of(new Author(1L, "name", "surname")));
        Mockito.when(authorDao.getAuthorsByBookId(2L)).thenReturn(List.of(new Author(2L, "Paul", "Wells")));
        Mockito.when(authorDao.getAuthorsByBookId(3L)).thenReturn(List.of(new Author(3L, "John", "Smith")));
        Mockito.when(authorDao.getAuthorsByBookId(4L)).thenReturn(List.of(new Author(4L, "Alex", "Nikolaev")));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        bookService = new BookService(daoFactory);
        String result = bookService.getBooksByLanguage(Locale.ENGLISH, page);
        assertEquals(
                "{\"elementsCount\":3," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2020-01-01\",\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}," +
                        "{\"id\":2,\"name\":\"another book\",\"count\":5,\"publicationDate\":\"2020-01-01\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":2,\"firstName\":\"Paul\",\"lastName\":\"Wells\"}],\"isbn\":\"new ISBN\"}," +
                        "{\"id\":3,\"name\":\"another book1\",\"count\":5,\"publicationDate\":\"2020-01-01\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":3,\"firstName\":\"John\",\"lastName\":\"Smith\"}],\"isbn\":\"new ISBN1\"}," +
                        "{\"id\":4,\"name\":\"another book2\",\"count\":5,\"publicationDate\":\"2020-01-01\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":4,\"firstName\":\"Alex\",\"lastName\":\"Nikolaev\"}],\"isbn\":\"new ISBN2\"}]}",
                result);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        daoFactory = Mockito.mock(DaoFactory.class);
        assertThrows(ConnectionDBException.class, () -> bookService.getBooksByLanguage(Locale.US, page));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.getBooksByLanguage(Locale.US, page));
    }

    @Test
    void getBooksSortedBy() throws ServiceException, JDBCException, ConnectionDBException {
        Page<Book> page = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .createPage();
        Date dateOfCreation = Utility.parseDateOrDefault("2020-01-01", new Date());
        Page<Book> resultBookPage = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setElementsCount(3)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setName("name")
                                .setCount(5)
                                .setPublicationDate(dateOfCreation)
                                .setFine(100.0)
                                .setLanguage("en_US")
                                .setISBN("ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(2L)
                                .setName("another book")
                                .setCount(5)
                                .setPublicationDate(dateOfCreation)
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(3L)
                                .setName("another book1")
                                .setCount(5)
                                .setPublicationDate(dateOfCreation)
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN1")
                                .createBook()))
                .createPage();
        Mockito.when(bookDao.getBooksSortedBy(page, "book_id")).thenReturn(resultBookPage);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(authorDao.getAuthorsByBookId(1L)).thenReturn(List.of(new Author(1L, "name", "surname")));
        Mockito.when(authorDao.getAuthorsByBookId(2L)).thenReturn(List.of(new Author(2L, "Paul", "Wells")));
        Mockito.when(authorDao.getAuthorsByBookId(3L)).thenReturn(List.of(new Author(3L, "John", "Smith")));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        bookService = new BookService(daoFactory);
        String result = bookService.getBooksSortedBy(Locale.ENGLISH, page, "book_id");
        assertEquals(
                "{\"elementsCount\":3," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2020-01-01\",\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}," +
                        "{\"id\":2,\"name\":\"another book\",\"count\":5,\"publicationDate\":\"2020-01-01\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":2,\"firstName\":\"Paul\",\"lastName\":\"Wells\"}],\"isbn\":\"new ISBN\"}," +
                        "{\"id\":3,\"name\":\"another book1\",\"count\":5,\"publicationDate\":\"2020-01-01\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":3,\"firstName\":\"John\",\"lastName\":\"Smith\"}],\"isbn\":\"new ISBN1\"}]}",
                result);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        daoFactory = Mockito.mock(DaoFactory.class);
        assertThrows(ConnectionDBException.class, () -> bookService.getBooksSortedBy(Locale.US, page, "book_id"));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.getBooksSortedBy(Locale.US, page, "book_id"));
    }

    @Test
    void createBook() throws SQLException, JDBCException, ServiceException, ConnectionDBException {
        Book book = Book.builder()
                .setId(id)
                .setName("name")
                .setCount(5)
                .setPublicationDate(new Date())
                .setFine(100.0)
                .setLanguage("en_US")
                .setISBN("ISBN")
                .createBook();
        Mockito.when(bookDao.create(book)).thenReturn(book.getId());
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        long result = bookService.createBook(book);
        assertEquals(id, result);
        Mockito.when(bookDao.create(book)).thenThrow(SQLException.class);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.createBook(book));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.createBook(book));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.createBook(book));
    }

    @Test
    void updateBook() throws SQLException, JDBCException {
        Book book = Book.builder()
                .setId(id)
                .setName("name")
                .setCount(5)
                .setPublicationDate(new Date())
                .setFine(100.0)
                .setLanguage("en_US")
                .setISBN("ISBN")
                .createBook();
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        assertDoesNotThrow(() -> bookService.updateBook(book));
        Mockito.doThrow(SQLException.class).when(bookDao).update(book);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.updateBook(book));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.updateBook(book));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.updateBook(book));
    }

    @Test
    void getBookById() throws JDBCException, ServiceException, ConnectionDBException {
        Book book = Book.builder()
                .setId(id)
                .setName("name")
                .setCount(5)
                .setPublicationDate(new Date())
                .setFine(100.0)
                .setLanguage("en_US")
                .setISBN("ISBN")
                .setAuthors(List.of(new Author(1L, "name", "surname")))
                .createBook();
        Mockito.when(bookDao.getById(id)).thenReturn(Optional.ofNullable(book));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Mockito.when(authorDao.getAuthorsByBookId(id)).thenReturn(List.of(new Author(1L, "name", "surname")));
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        Book result = bookService.getBookById(id);
        assertEquals(book, result);
        Mockito.when(bookDao.getById(id)).thenReturn(Optional.empty());
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.getBookById(id));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.getBookById(id));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.getBookById(id));
    }

    @Test
    void getBookPage() throws JDBCException, ServiceException, ConnectionDBException {
        Date dateCreated = Utility.parseDateOrDefault("2020-10-12", new Date());
        Book book = Book.builder()
                .setId(id)
                .setName("name")
                .setCount(5)
                .setPublicationDate(dateCreated)
                .setFine(100.0)
                .setLanguage("en_US")
                .setISBN("ISBN")
                .setAuthors(List.of(new Author(1L, "name", "surname")))
                .createBook();
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .createPage();
        Page<Book> resultingPage = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(book))
                .createPage();
        Mockito.when(bookDao.getPage(page)).thenReturn(resultingPage);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(authorDao.getAuthorsByBookId(id)).thenReturn(List.of(new Author(1L, "name", "surname")));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        bookService = new BookService(daoFactory);
        String result = bookService.getBookPage(Locale.ENGLISH, page);
        assertEquals(
                "{\"elementsCount\":1," +
                        "\"limit\":10," +
                        "\"content\":[" +
                        "{\"id\":0,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2020-10-12\"," +
                        "\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[" +
                        "{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}]}", result);
        Mockito.when(bookDao.getPage(page)).thenReturn(Page.<Book>builder().setLimit(10).setData(new ArrayList<>()).createPage());
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        result = bookService.getBookPage(Locale.ENGLISH, page);
        assertEquals("{\"elementsCount\":0,\"limit\":10,\"content\":[]}", result);
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.getBookPage(Locale.ENGLISH, page));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.getBookPage(Locale.ENGLISH, page));
    }

    @Test
    void deleteBook() throws JDBCException {
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        assertDoesNotThrow(() -> bookService.deleteBook(id));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.deleteBook(id));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.deleteBook(id));
    }

    @Test
    void getBookPageWhichUserCanOrder() throws JDBCException, ServiceException, ConnectionDBException {
        Date dateCreated = Utility.parseDateOrDefault("2020-10-12", new Date());
        Book book = Book.builder()
                .setId(id)
                .setName("name")
                .setCount(5)
                .setPublicationDate(dateCreated)
                .setFine(100.0)
                .setLanguage("en_US")
                .setISBN("ISBN")
                .setAuthors(List.of(new Author(1L, "name", "surname")))
                .createBook();
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .createPage();
        Page<Book> resultingPage = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(book))
                .createPage();
        Mockito.when(bookDao.getBooksWhichUserDidNotOrder(page, id, "book_name")).thenReturn(resultingPage);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(authorDao.getAuthorsByBookId(id)).thenReturn(List.of(new Author(1L, "name", "surname")));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        bookService = new BookService(daoFactory);
        String result = bookService.getBookPageWhichUserCanOrder(Locale.ENGLISH, page, id, "book_name");
        assertEquals(
                "{\"elementsCount\":1," +
                        "\"limit\":10," +
                        "\"content\":[" +
                        "{\"id\":0,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2020-10-12\"," +
                        "\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[" +
                        "{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}]}", result);
        Mockito.when(bookDao.getBooksWhichUserDidNotOrder(page, id, "book_name")).thenReturn(Page.<Book>builder().setLimit(10).setData(new ArrayList<>()).createPage());
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        bookService = new BookService(daoFactory);
        result = bookService.getBookPageWhichUserCanOrder(Locale.ENGLISH, page, id, "book_name");
        assertEquals("{\"elementsCount\":0,\"limit\":10,\"content\":[]}", result);
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ConnectionDBException.class, () -> bookService.getBookPageWhichUserCanOrder(Locale.ENGLISH, page, id, "book_name"));
        daoFactory = Mockito.mock(DaoFactory.class);
        Mockito.when(daoFactory.createBookDao()).thenThrow(DaoException.class);
        bookService = new BookService(daoFactory);
        assertThrows(ServiceException.class, () -> bookService.getBookPageWhichUserCanOrder(Locale.ENGLISH, page, id, "book_name"));
    }
}