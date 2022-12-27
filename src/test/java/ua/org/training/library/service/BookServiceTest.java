package ua.org.training.library.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private long id = 0;
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    private BookService bookService;

    @Test
    void getBooksByAuthorId() throws JDBCException, ServiceException {
        Page<Book> page = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .createPage();
        Page<Book> resultBookPage = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setElementsCount(2)
                .setData(List.of(new Book(1L, "name", 5, "ISBN", new Date(), 100.0, "en_US", null),
                        new Book(2L, "another book", 5, "new ISBN", new Date(), 10.0, "en_US", null)))
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
                        ":[{\"id\":1,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2023-01-06\"," +
                        "\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":" +
                        "[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}," +
                        "{\"id\":2,\"name\":\"another book\",\"count\":5,\"publicationDate\":\"2023-01-06\"," +
                        "\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"new ISBN\"}]}",
                result);
    }

    @Test
    void getBooksByAuthorIdAndLanguage() throws JDBCException, ServiceException {
        Page<Book> page = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .createPage();
        Page<Book> resultBookPage = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setElementsCount(3)
                .setData(List.of(new Book(1L, "name", 5, "ISBN", new Date(), 100.0, "en_US", null),
                        new Book(2L, "another book", 5, "new ISBN", new Date(), 10.0, "en_US", null),
                        new Book(3L, "another book1", 5, "new ISBN1", new Date(), 10.0, "en_US", null)))
                .createPage();
        Mockito.when(bookDao.getBooksByAuthorIdAndLanguage(page, 5L, "en_US")).thenReturn(resultBookPage);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(authorDao.getAuthorsByBookId(1L)).thenReturn(List.of(new Author(5L, "Alex", "Nikolaev")));
        Mockito.when(authorDao.getAuthorsByBookId(2L)).thenReturn(List.of(new Author(5L, "Alex", "Nikolaev")));
        Mockito.when(authorDao.getAuthorsByBookId(3L)).thenReturn(List.of(new Author(5L, "Alex", "Nikolaev")));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        bookService = new BookService(daoFactory);
        String result = bookService.getBooksByAuthorIdAndLanguage(Locale.ENGLISH, page, 5L);
        assertEquals(
                "{\"elementsCount\":3," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2023-01-06\"," +
                        "\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":5,\"firstName\":\"Alex\",\"lastName\":\"Nikolaev\"}],\"isbn\":\"ISBN\"}," +
                        "{\"id\":2,\"name\":\"another book\",\"count\":5,\"publicationDate\":\"2023-01-06\"," +
                        "\"fine\":\"10.0 USD\",\"language\":\"en_US\",\"authors\":[" +
                        "{\"id\":5,\"firstName\":\"Alex\",\"lastName\":\"Nikolaev\"}],\"isbn\":\"new ISBN\"}," +
                        "{\"id\":3,\"name\":\"another book1\",\"count\":5,\"publicationDate\":\"2023-01-06\"," +
                        "\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":5,\"firstName\":\"Alex\",\"lastName\":\"Nikolaev\"}],\"isbn\":\"new ISBN1\"}]}",
                result);
    }

    @Test
    void getBooksByLanguage() throws JDBCException, ServiceException {
        Page<Book> page = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .createPage();
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
                                .setPublicationDate(new Date())
                                .setFine(100.0)
                                .setLanguage("en_US")
                                .setISBN("ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(2L)
                                .setName("another book")
                                .setCount(5)
                                .setPublicationDate(new Date())
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(3L)
                                .setName("another book1")
                                .setCount(5)
                                .setPublicationDate(new Date())
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN1")
                                .createBook(),
                        Book.builder()
                                .setId(4L)
                                .setName("another book2")
                                .setCount(5)
                                .setPublicationDate(new Date())
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
                        "{\"id\":1,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2023-01-06\",\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}," +
                        "{\"id\":2,\"name\":\"another book\",\"count\":5,\"publicationDate\":\"2023-01-06\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":2,\"firstName\":\"Paul\",\"lastName\":\"Wells\"}],\"isbn\":\"new ISBN\"}," +
                        "{\"id\":3,\"name\":\"another book1\",\"count\":5,\"publicationDate\":\"2023-01-06\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":3,\"firstName\":\"John\",\"lastName\":\"Smith\"}],\"isbn\":\"new ISBN1\"}," +
                        "{\"id\":4,\"name\":\"another book2\",\"count\":5,\"publicationDate\":\"2023-01-06\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":4,\"firstName\":\"Alex\",\"lastName\":\"Nikolaev\"}],\"isbn\":\"new ISBN2\"}]}",
                result);
    }

    @Test
    void getBooksSortedBy() throws ServiceException, JDBCException {
        Page<Book> page = Page.<Book>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .createPage();
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
                                .setPublicationDate(new Date())
                                .setFine(100.0)
                                .setLanguage("en_US")
                                .setISBN("ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(2L)
                                .setName("another book")
                                .setCount(5)
                                .setPublicationDate(new Date())
                                .setFine(10.0)
                                .setLanguage("en_US")
                                .setISBN("new ISBN")
                                .createBook(),
                        Book.builder()
                                .setId(3L)
                                .setName("another book1")
                                .setCount(5)
                                .setPublicationDate(new Date())
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
                        "{\"id\":1,\"name\":\"name\",\"count\":5,\"publicationDate\":\"2023-01-06\",\"fine\":\"100.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":1,\"firstName\":\"name\",\"lastName\":\"surname\"}],\"isbn\":\"ISBN\"}," +
                        "{\"id\":2,\"name\":\"another book\",\"count\":5,\"publicationDate\":\"2023-01-06\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":2,\"firstName\":\"Paul\",\"lastName\":\"Wells\"}],\"isbn\":\"new ISBN\"}," +
                        "{\"id\":3,\"name\":\"another book1\",\"count\":5,\"publicationDate\":\"2023-01-06\",\"fine\":\"10.0 USD\",\"language\":\"en_US\"," +
                        "\"authors\":[{\"id\":3,\"firstName\":\"John\",\"lastName\":\"Smith\"}],\"isbn\":\"new ISBN1\"}]}",
                result);
    }

    @Test
    void createBook() throws SQLException, JDBCException, ServiceException {
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
    }

    @Test
    void getBookById() throws JDBCException, ServiceException {
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
    }

    @Test
    void getBookPage() {
    }

    @Test
    void deleteBook() {
    }
}