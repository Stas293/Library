package ua.org.training.library.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    private AuthorService authorService;

    @Test
    void getAllAuthors() throws JDBCException, ServiceException, ConnectionDBException {
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Mockito.when(authorDao.getAuthorsByBookIsbn(Mockito.anyString())).thenReturn(List.of(
                Author.builder().setId(1L).setFirstName("John").setLastName("Doe").createAuthor(),
                Author.builder().setId(2L).setFirstName("Jane").setLastName("Doe").createAuthor()
        ));
        authorService = new AuthorService(daoFactory);
        assertEquals(2, authorService.getAllAuthors("123").size());

        Mockito.when(authorDao.getAuthorsByBookIsbn(Mockito.anyString())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> authorService.getAllAuthors("123"));

        Mockito.when(daoFactory.createAuthorDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> authorService.getAllAuthors("123"));
    }

    @Test
    void createAuthor() throws ServiceException, JDBCException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Mockito.when(authorDao.create(Mockito.any(Author.class))).thenReturn(1L);
        authorService = new AuthorService(daoFactory);
        assertEquals(1L, authorService.createAuthor(Author.builder().createAuthor()));

        Mockito.when(authorDao.create(Mockito.any(Author.class))).thenThrow(SQLException.class);
        assertThrows(ConnectionDBException.class, () -> authorService.createAuthor(Author.builder().createAuthor()));

        Mockito.when(authorDao.create(Mockito.any(Author.class))).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> authorService.createAuthor(Author.builder().createAuthor()));

        Mockito.when(daoFactory.createAuthorDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> authorService.createAuthor(Author.builder().createAuthor()));
    }

    @Test
    void getAuthorById() throws JDBCException, ServiceException, ConnectionDBException {
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Mockito.when(authorDao.getById(Mockito.anyLong())).thenReturn(
                Optional.ofNullable(Author.builder().setId(1L).setFirstName("John").setLastName("Doe").createAuthor())
        );
        authorService = new AuthorService(daoFactory);
        assertEquals(1L, authorService.getAuthorById(1L).getId());

        Mockito.when(authorDao.getById(Mockito.anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> authorService.getAuthorById(1L));

        Mockito.when(daoFactory.createAuthorDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> authorService.getAuthorById(1L));
    }

    @Test
    void getAuthorPage() throws JDBCException, ServiceException, ConnectionDBException {
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Mockito.when(authorDao.getPage(Mockito.any())).thenReturn(
                new Page<>(0, 5, "ASC", "", 2, List.of(
                        Author.builder().setId(1L).setFirstName("John").setLastName("Doe").createAuthor(),
                        Author.builder().setId(2L).setFirstName("Jane").setLastName("Doe").createAuthor()
                ))
        );
        authorService = new AuthorService(daoFactory);
        assertEquals("{" +
                "\"elementsCount\":2," +
                "\"limit\":5," +
                "\"content\":[" +
                "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"fullName\":\"John Doe\"}," +
                "{\"id\":2,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"fullName\":\"Jane Doe\"}]}",
                authorService.getAuthorPage(
                        Page.<Author>builder()
                                .setLimit(5)
                                .setPageNumber(0)
                                .setSorting("ASC")
                                .setSearch("")
                                .createPage()
                )
        );

        Mockito.when(authorDao.getPage(Mockito.any())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> authorService.getAuthorPage(
                Page.<Author>builder()
                        .setLimit(5)
                        .setPageNumber(0)
                        .setSorting("ASC")
                        .setSearch("")
                        .createPage()
        ));

        Mockito.when(daoFactory.createAuthorDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> authorService.getAuthorPage(
                Page.<Author>builder()
                        .setLimit(5)
                        .setPageNumber(0)
                        .setSorting("ASC")
                        .setSearch("")
                        .createPage()
        ));
    }

    @Test
    void updateAuthor() throws JDBCException, SQLException {
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        authorService = new AuthorService(daoFactory);
        assertDoesNotThrow(() -> authorService.updateAuthor(Author.builder().createAuthor()));

        Mockito.doThrow(SQLException.class).when(authorDao).update(Mockito.any(Author.class));
        assertThrows(ConnectionDBException.class, () -> authorService.updateAuthor(Author.builder().createAuthor()));

        Mockito.doThrow(DaoException.class).when(authorDao).update(Mockito.any(Author.class));
        assertThrows(ServiceException.class, () -> authorService.updateAuthor(Author.builder().createAuthor()));
    }

    @Test
    void deleteAuthor() throws SQLException, JDBCException {
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        authorService = new AuthorService(daoFactory);
        assertDoesNotThrow(() -> authorService.deleteAuthor(1L));

        Mockito.doThrow(SQLException.class).when(authorDao).delete(Mockito.anyLong());
        assertThrows(ConnectionDBException.class, () -> authorService.deleteAuthor(1L));

        Mockito.doThrow(DaoException.class).when(authorDao).delete(Mockito.anyLong());
        assertThrows(ServiceException.class, () -> authorService.deleteAuthor(1L));
    }
}