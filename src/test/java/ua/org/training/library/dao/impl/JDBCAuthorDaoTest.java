package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JDBCAuthorDaoTest {
    private static AuthorDao authorDao;

    @BeforeEach
    void setUp() {
        try {
            authorDao = DaoFactory.getInstance().createAuthorDao();
        } catch (JDBCException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAuthorsByBookId() throws JDBCException {
        assertEquals(
                List.of(
                        Author.builder()
                                .setId(1L)
                                .setFirstName("Joanne")
                                .setLastName("Rowling")
                                .createAuthor()),
                authorDao.getAuthorsByBookId(1L));
        authorDao = DaoFactory.getInstance().createAuthorDao();
        assertEquals(
                List.of(
                        Author.builder()
                                .setId(3L)
                                .setFirstName("Tad")
                                .setLastName("Burness")
                                .createAuthor(),
                        Author.builder()
                                .setId(4L)
                                .setFirstName("Matthew")
                                .setLastName("Stone")
                                .createAuthor()),
                authorDao.getAuthorsByBookId(3L));
        authorDao = DaoFactory.getInstance().createAuthorDao();
        assertEquals(List.of(), authorDao.getAuthorsByBookId(18478142365L));
    }

    @Test
    void getAuthorsByBookIsbn() throws JDBCException {
        assertEquals(
                List.of(Author.builder()
                        .setId(1L)
                        .setFirstName("Joanne")
                        .setLastName("Rowling")
                        .createAuthor()),
                authorDao.getAuthorsByBookIsbn("9780747532743"));
        authorDao = DaoFactory.getInstance().createAuthorDao();
        assertEquals(
                List.of(Author.builder()
                                .setId(3L)
                                .setFirstName("Tad")
                                .setLastName("Burness")
                                .createAuthor(),
                        Author.builder()
                                .setId(4L)
                                .setFirstName("Matthew")
                                .setLastName("Stone")
                                .createAuthor()),
                authorDao.getAuthorsByBookIsbn("9780316430913"));
        authorDao = DaoFactory.getInstance().createAuthorDao();
        assertEquals(List.of(), authorDao.getAuthorsByBookIsbn("237587236587236785"));
        authorDao = DaoFactory.getInstance().createAuthorDao();
        assertEquals(List.of(), authorDao.getAuthorsByBookIsbn("agjhsfahjfghk"));
    }

    @Test
    void create() {
        Author author = Author.builder()
                .setFirstName("Test")
                .setLastName("Test")
                .createAuthor();
        try {
            assertEquals(
                    8L,
                    authorDao.create(author));
        } catch (SQLException | DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getById() throws JDBCException {
        assertEquals(
                Optional.of(
                        Author.builder()
                                .setId(1L)
                                .setFirstName("Joanne")
                                .setLastName("Rowling")
                                .createAuthor()),
                authorDao.getById(1L));
        authorDao = DaoFactory.getInstance().createAuthorDao();
        assertEquals(
                Optional.empty(),
                authorDao.getById(123456789L));
    }

    @Test
    void getPage() {
        Page<Author> page = Page.<Author>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("e")
                .setSorting("ASC")
                .createPage();
        Page<Author> result = Page.<Author>builder()
                .setPageNumber(0)
                .setLimit(4)
                .setSearch("e")
                .setSorting("ASC")
                .setElementsCount(4)
                .setData(List.of(
                        Author.builder()
                                .setId(3L)
                                .setFirstName("Tad")
                                .setLastName("Burness")
                                .createAuthor(),
                        Author.builder()
                                .setId(2L)
                                .setFirstName("Jennifer")
                                .setLastName("Firestone")
                                .createAuthor(),
                        Author.builder()
                                .setId(1L)
                                .setFirstName("Joanne")
                                .setLastName("Rowling")
                                .createAuthor(),
                        Author.builder()
                                .setId(4L)
                                .setFirstName("Matthew")
                                .setLastName("Stone")
                                .createAuthor())
                )
                .createPage();
        assertEquals(result,
                authorDao.getPage(page));
    }

    @Test
    void update() {
        Author author = Author.builder()
                .setId(150L)
                .setFirstName("Joanne")
                .setLastName("Rowling")
                .createAuthor();
        assertDoesNotThrow(() -> authorDao.update(author));
    }

    @Test
    void delete() throws JDBCException {
        assertDoesNotThrow(() -> authorDao.delete(150L));
        authorDao = DaoFactory.getInstance().createAuthorDao();
        assertDoesNotThrow(() -> authorDao.delete(6L));
    }
}