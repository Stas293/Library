package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCBookDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private CallableStatement callableStatement;
    @Mock
    private ResultSet resultSet;
    private BookDao bookDao;

    @Test
    void getBooksByAuthorId() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.doNothing().when(callableStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("book_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("book_name");
        Mockito.when(resultSet.getInt("book_count")).thenReturn(1);
        Mockito.when(resultSet.getString("ISBN")).thenReturn("ISBN");
        Mockito.when(resultSet.getTimestamp("book_date_publication")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getDouble("fine_per_day")).thenReturn(1.0);
        Mockito.when(resultSet.getString("language")).thenReturn("language");
        Mockito.when(callableStatement.getLong(6)).thenReturn(1L);

        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        Page<Book> result = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .setElementsCount(1)
                .setData(
                        List.of(
                                Book.builder()
                                        .setId(1L)
                                        .setName("book_name")
                                        .setCount(1)
                                        .setISBN("ISBN")
                                        .setPublicationDate(new Timestamp(1))
                                        .setFine(1.0)
                                        .setLanguage("language")
                                        .createBook()
                        )
                ).createPage();
        bookDao = new JDBCBookDao(connection);
        bookDao.getBooksByAuthorId(page, 1L);
        assertEquals(result, page);
        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("book_id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(1)).getInt("book_count");
        Mockito.verify(resultSet, Mockito.times(1)).getString("ISBN");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("book_date_publication");
        Mockito.verify(resultSet, Mockito.times(1)).getDouble("fine_per_day");
        Mockito.verify(resultSet, Mockito.times(1)).getString("language");

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBooksByAuthorId(page, 1L));
    }

    @Test
    void getBooksByLanguage() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.doNothing().when(callableStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("book_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("book_name");
        Mockito.when(resultSet.getInt("book_count")).thenReturn(1);
        Mockito.when(resultSet.getString("ISBN")).thenReturn("ISBN");
        Mockito.when(resultSet.getTimestamp("book_date_publication")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getDouble("fine_per_day")).thenReturn(1.0);
        Mockito.when(resultSet.getString("language")).thenReturn("language");
        Mockito.when(callableStatement.getLong(6)).thenReturn(1L);

        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        Page<Book> result = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .setElementsCount(1)
                .setData(
                        List.of(
                                Book.builder()
                                        .setId(1L)
                                        .setName("book_name")
                                        .setCount(1)
                                        .setISBN("ISBN")
                                        .setPublicationDate(new Timestamp(1))
                                        .setFine(1.0)
                                        .setLanguage("language")
                                        .createBook()
                        )
                ).createPage();
        bookDao = new JDBCBookDao(connection);
        assertEquals(result, bookDao.getBooksByLanguage(page, "language"));
        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("book_id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(1)).getInt("book_count");
        Mockito.verify(resultSet, Mockito.times(1)).getString("ISBN");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("book_date_publication");
        Mockito.verify(resultSet, Mockito.times(1)).getDouble("fine_per_day");
        Mockito.verify(resultSet, Mockito.times(1)).getString("language");

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBooksByLanguage(page, "language"));
    }

    @Test
    void getBooksSortedBy() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.doNothing().when(callableStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("book_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("book_name");
        Mockito.when(resultSet.getInt("book_count")).thenReturn(1);
        Mockito.when(resultSet.getString("ISBN")).thenReturn("ISBN");
        Mockito.when(resultSet.getTimestamp("book_date_publication")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getDouble("fine_per_day")).thenReturn(1.0);
        Mockito.when(resultSet.getString("language")).thenReturn("language");
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet1);
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getLong(1)).thenReturn(1L);
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        Page<Book> result = Page.<Book>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .setElementsCount(1)
                .setData(
                        List.of(
                                Book.builder()
                                        .setId(1L)
                                        .setName("book_name")
                                        .setCount(1)
                                        .setISBN("ISBN")
                                        .setPublicationDate(new Timestamp(1))
                                        .setFine(1.0)
                                        .setLanguage("language")
                                        .createBook()
                        )
                ).createPage();
        bookDao = new JDBCBookDao(connection);
        assertEquals(result, bookDao.getBooksSortedBy(page, "book_name"));
        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("book_id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(1)).getInt("book_count");
        Mockito.verify(resultSet, Mockito.times(1)).getString("ISBN");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("book_date_publication");
        Mockito.verify(resultSet, Mockito.times(1)).getDouble("fine_per_day");
        Mockito.verify(resultSet, Mockito.times(1)).getString("language");
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet1, Mockito.times(1)).next();
        Mockito.verify(resultSet1, Mockito.times(1)).getLong(1);

        Mockito.when(resultSet.next()).thenReturn(false);
        Mockito.when(resultSet1.next()).thenReturn(false);
        assertEquals(0, bookDao.getBooksSortedBy(page, "book_name").getData().size());

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBooksSortedBy(page, "book_name"));

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBooksSortedBy(page, "book_name"));
    }

    @Test
    void getBookByOrderId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("book_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("book_name");
        Mockito.when(resultSet.getInt("book_count")).thenReturn(1);
        Mockito.when(resultSet.getString("ISBN")).thenReturn("ISBN");
        Mockito.when(resultSet.getTimestamp("book_date_publication")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getDouble("fine_per_day")).thenReturn(1.0);
        Mockito.when(resultSet.getString("language")).thenReturn("language");
        bookDao = new JDBCBookDao(connection);
        assertEquals(Optional.of(
                Book.builder()
                        .setId(1L)
                        .setName("book_name")
                        .setCount(1)
                        .setISBN("ISBN")
                        .setPublicationDate(new Timestamp(1))
                        .setFine(1.0)
                        .setLanguage("language")
                        .createBook()
                ),
                bookDao.getBookByOrderId(1L)
        );
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("book_id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(1)).getInt("book_count");
        Mockito.verify(resultSet, Mockito.times(1)).getString("ISBN");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("book_date_publication");
        Mockito.verify(resultSet, Mockito.times(1)).getDouble("fine_per_day");
        Mockito.verify(resultSet, Mockito.times(1)).getString("language");
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), bookDao.getBookByOrderId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBookByOrderId(1L));
    }

    @Test
    void getBooksWhichUserDidNotOrder() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.doNothing().when(callableStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.doNothing().when(callableStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("book_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("book_name");
        Mockito.when(resultSet.getInt("book_count")).thenReturn(1);
        Mockito.when(resultSet.getString("ISBN")).thenReturn("ISBN");
        Mockito.when(resultSet.getTimestamp("book_date_publication")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getDouble("fine_per_day")).thenReturn(1.0);
        Mockito.when(resultSet.getString("language")).thenReturn("language");
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet1);
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getLong(1)).thenReturn(2L);
        bookDao = new JDBCBookDao(connection);
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setSearch("")
                .setLimit(5)
                .setSorting("ASC")
                .createPage();
        Page<Book> resultPage = Page.<Book>builder()
                .setPageNumber(0)
                .setSearch("")
                .setLimit(5)
                .setSorting("ASC")
                .setElementsCount(2)
                .setData(List.of(
                        Book.builder()
                                .setId(1L)
                                .setName("book_name")
                                .setCount(1)
                                .setISBN("ISBN")
                                .setPublicationDate(new Timestamp(1))
                                .setFine(1.0)
                                .setLanguage("language")
                                .createBook(),
                        Book.builder()
                                .setId(1L)
                                .setName("book_name")
                                .setCount(1)
                                .setISBN("ISBN")
                                .setPublicationDate(new Timestamp(1))
                                .setFine(1.0)
                                .setLanguage("language")
                                .createBook()
                )).createPage();
        assertEquals(resultPage, bookDao.getBooksWhichUserDidNotOrder(page, 1L, "book_name"));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong("book_id");
        Mockito.verify(resultSet, Mockito.times(2)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(2)).getInt("book_count");
        Mockito.verify(resultSet, Mockito.times(2)).getString("ISBN");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("book_date_publication");
        Mockito.verify(resultSet, Mockito.times(2)).getDouble("fine_per_day");
        Mockito.verify(resultSet, Mockito.times(2)).getString("language");
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet1, Mockito.times(1)).next();
        Mockito.verify(resultSet1, Mockito.times(1)).getLong(1);

        Mockito.when(resultSet.next()).thenReturn(false);
        assertTrue(bookDao.getBooksWhichUserDidNotOrder(page, 1L, "book_name").getData().isEmpty());

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBooksWhichUserDidNotOrder(page, 1L, "book_name"));

        Mockito.when(resultSet.next()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBooksWhichUserDidNotOrder(page, 1L, "book_name"));

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBooksWhichUserDidNotOrder(page, 1L, "book_name"));
    }

    @Test
    void create() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Field INSERT_BOOK = JDBCBookDao.class.getDeclaredField("INSERT_BOOK");
        INSERT_BOOK.setAccessible(true);
        Mockito.when(connection.prepareStatement(INSERT_BOOK.get("String").toString(), Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Field INSERT_BOOK_AUTHOR = JDBCBookDao.class.getDeclaredField("INSERT_BOOK_AUTHOR");
        INSERT_BOOK_AUTHOR.setAccessible(true);
        PreparedStatement preparedStatement1 = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(INSERT_BOOK_AUTHOR.get("String").toString())).thenReturn(preparedStatement1);
        Mockito.doNothing().when(preparedStatement1).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.doNothing().when(preparedStatement1).addBatch();
        Mockito.when(preparedStatement1.executeBatch()).thenReturn(new int[]{1});
        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet1);
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getLong(1)).thenReturn(1L);
        Book book = Book.builder()
                .setName("book_name")
                .setCount(1)
                .setISBN("ISBN")
                .setPublicationDate(new Timestamp(1))
                .setFine(1.0)
                .setLanguage("language")
                .setAuthors(List.of(
                        Author.builder()
                                .setId(1L)
                                .setFirstName("author_name")
                                .setLastName("author_surname")
                                .createAuthor()
                ))
                .createBook();
        bookDao = new JDBCBookDao(connection);
        assertEquals(1, bookDao.create(book));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString(), Mockito.anyInt());
        Mockito.verify(preparedStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(preparedStatement, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        Mockito.verify(preparedStatement, Mockito.times(1)).setDouble(Mockito.anyInt(), Mockito.anyDouble());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(preparedStatement, Mockito.times(1)).getGeneratedKeys();
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement1, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement1, Mockito.times(1)).addBatch();
        Mockito.verify(preparedStatement1, Mockito.times(1)).executeBatch();
        Mockito.verify(preparedStatement, Mockito.times(1)).getGeneratedKeys();
        Mockito.verify(resultSet1, Mockito.times(1)).next();
        Mockito.verify(resultSet1, Mockito.times(1)).getLong(1);

        Mockito.when(preparedStatement1.executeBatch()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.create(book));

        ResultSet resultSet2 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet2);
        Mockito.when(resultSet2.next()).thenReturn(false);
        assertEquals(Constants.APP_DEFAULT_ID, bookDao.create(book));

        Mockito.when(preparedStatement.getGeneratedKeys()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.create(book));

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.create(book));

        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.create(book));


    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("book_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("book_name");
        Mockito.when(resultSet.getInt("book_count")).thenReturn(1);
        Mockito.when(resultSet.getString("ISBN")).thenReturn("ISBN");
        Mockito.when(resultSet.getTimestamp("book_date_publication")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getDouble("fine_per_day")).thenReturn(1.0);
        Mockito.when(resultSet.getString("language")).thenReturn("language");
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Book book = Book.builder()
                .setId(1L)
                .setName("book_name")
                .setCount(1)
                .setISBN("ISBN")
                .setPublicationDate(new Timestamp(1))
                .setFine(1.0)
                .setLanguage("language")
                .createBook();
        bookDao = new JDBCBookDao(connection);
        assertEquals(Optional.of(book), bookDao.getById(1L));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("book_id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(1)).getInt("book_count");
        Mockito.verify(resultSet, Mockito.times(1)).getString("ISBN");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("book_date_publication");
        Mockito.verify(resultSet, Mockito.times(1)).getDouble("fine_per_day");
        Mockito.verify(resultSet, Mockito.times(1)).getString("language");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), bookDao.getById(1L));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getById(1L));
    }

    @Test
    void getPage() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("book_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("book_name");
        Mockito.when(resultSet.getInt("book_count")).thenReturn(1);
        Mockito.when(resultSet.getString("ISBN")).thenReturn("ISBN");
        Mockito.when(resultSet.getTimestamp("book_date_publication")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getDouble("fine_per_day")).thenReturn(1.0);
        Mockito.when(resultSet.getString("language")).thenReturn("language");
        Mockito.doNothing().when(callableStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet1);
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getLong(1)).thenReturn(1L);
        Page<Book> page = Page.<Book>builder()
                .setPageNumber(0)
                .setSorting("ASC")
                .setLimit(5)
                .setSearch("search")
                .createPage();
        Book book = Book.builder()
                .setId(1L)
                .setName("book_name")
                .setCount(1)
                .setISBN("ISBN")
                .setPublicationDate(new Timestamp(1))
                .setFine(1.0)
                .setLanguage("language")
                .createBook();
        Page<Book> expected = Page.<Book>builder()
                .setPageNumber(0)
                .setSorting("ASC")
                .setLimit(5)
                .setSearch("search")
                .setData(List.of(book))
                .setElementsCount(1)
                .createPage();
        bookDao = new JDBCBookDao(connection);
        assertEquals(expected, bookDao.getPage(page));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("book_id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(1)).getInt("book_count");
        Mockito.verify(resultSet, Mockito.times(1)).getString("ISBN");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("book_date_publication");
        Mockito.verify(resultSet, Mockito.times(1)).getDouble("fine_per_day");
        Mockito.verify(resultSet, Mockito.times(1)).getString("language");
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet1, Mockito.times(1)).next();
        Mockito.verify(resultSet1, Mockito.times(1)).getLong(1);

        Mockito.when(resultSet.next()).thenReturn(false);
        Mockito.when(resultSet1.next()).thenReturn(false);
        assertTrue(bookDao.getPage(page).getData().isEmpty());

        Mockito.when(callableStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getPage(page));

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getPage(page));
    }

    @Test
    void update() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Mockito.doNothing().when(connection).setAutoCommit(Mockito.anyBoolean());
        Field UPDATE_BOOK = JDBCBookDao.class.getDeclaredField("UPDATE_BOOK");
        UPDATE_BOOK.setAccessible(true);
        Mockito.when(connection.prepareStatement(UPDATE_BOOK.get("String").toString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doNothing().when(preparedStatement).setTimestamp(Mockito.anyInt(), Mockito.any());
        Mockito.doNothing().when(preparedStatement).setDouble(Mockito.anyInt(), Mockito.anyDouble());
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Field DELETE_BOOK_AUTHOR = JDBCBookDao.class.getDeclaredField("DELETE_BOOK_AUTHOR");
        DELETE_BOOK_AUTHOR.setAccessible(true);
        PreparedStatement preparedStatement1 = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(DELETE_BOOK_AUTHOR.get("String").toString())).thenReturn(preparedStatement1);
        Mockito.doNothing().when(preparedStatement1).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement1.executeUpdate()).thenReturn(1);
        Field INSERT_BOOK_AUTHOR = JDBCBookDao.class.getDeclaredField("INSERT_BOOK_AUTHOR");
        INSERT_BOOK_AUTHOR.setAccessible(true);
        PreparedStatement preparedStatement2 = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(INSERT_BOOK_AUTHOR.get("String").toString())).thenReturn(preparedStatement2);
        Mockito.doNothing().when(preparedStatement2).setLong(Mockito.anyInt(), Mockito.anyLong());
        Book book = Book.builder()
                .setId(1L)
                .setName("book_name")
                .setCount(1)
                .setISBN("ISBN")
                .setPublicationDate(new Timestamp(1))
                .setFine(1.0)
                .setLanguage("language")
                .setAuthors(List.of(
                        Author.builder()
                                .setId(1L)
                                .setFirstName("name")
                                .setLastName("surname")
                                .createAuthor()
                ))
                .createBook();
        bookDao = new JDBCBookDao(connection);
        assertDoesNotThrow(() -> bookDao.update(book));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(UPDATE_BOOK.get("String").toString());
        Mockito.verify(preparedStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(preparedStatement, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any());
        Mockito.verify(preparedStatement, Mockito.times(1)).setDouble(Mockito.anyInt(), Mockito.anyDouble());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(DELETE_BOOK_AUTHOR.get("String").toString());
        Mockito.verify(preparedStatement1, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement1, Mockito.times(1)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(INSERT_BOOK_AUTHOR.get("String").toString());
        Mockito.verify(preparedStatement2, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement2, Mockito.times(1)).executeBatch();
        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(connection.prepareStatement(DELETE_BOOK_AUTHOR.get("String").toString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.update(book));

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.update(book));

        Mockito.when(connection.prepareStatement(UPDATE_BOOK.get("String").toString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.update(book));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> bookDao.update(book));
    }

    @Test
    void delete() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Mockito.doNothing().when(connection).setAutoCommit(Mockito.anyBoolean());
        Field DELETE_BOOK = JDBCBookDao.class.getDeclaredField("DELETE_BOOK");
        DELETE_BOOK.setAccessible(true);
        Field DELETE_BOOK_AUTHOR = JDBCBookDao.class.getDeclaredField("DELETE_BOOK_AUTHOR");
        DELETE_BOOK_AUTHOR.setAccessible(true);
        Mockito.when(connection.prepareStatement(DELETE_BOOK_AUTHOR.get("String").toString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        PreparedStatement preparedStatement1 = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(DELETE_BOOK.get("String").toString())).thenReturn(preparedStatement1);
        Mockito.doNothing().when(preparedStatement1).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement1.executeUpdate()).thenReturn(1);
        bookDao = new JDBCBookDao(connection);
        assertDoesNotThrow(() -> bookDao.delete(1L));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(DELETE_BOOK_AUTHOR.get("String").toString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(DELETE_BOOK.get("String").toString());
        Mockito.verify(preparedStatement1, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement1, Mockito.times(1)).executeUpdate();
        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.delete(1L));

        Mockito.when(connection.prepareStatement(DELETE_BOOK_AUTHOR.get("String").toString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.delete(1L));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> bookDao.delete(1L));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        bookDao = new JDBCBookDao(connection);
        assertDoesNotThrow(() -> bookDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> bookDao.close());
    }

    @Test
    void getBookByISBN() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("test");
        Mockito.when(resultSet.getTimestamp(Mockito.anyString())).thenReturn(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1)));
        Mockito.when(resultSet.getInt(Mockito.anyString())).thenReturn(1);
        Mockito.when(resultSet.getDouble(Mockito.anyString())).thenReturn(1.0);

        bookDao = new JDBCBookDao(connection);
        Optional<Book> book = bookDao.getBookByISBN("test");
        Book expected = Book.builder()
                .setId(1L)
                .setName("test")
                .setISBN("test")
                .setPublicationDate(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1)))
                .setCount(1)
                .setFine(1)
                .setLanguage("test")
                .setAuthors(null)
                .createBook();
        assertEquals(Optional.of(expected), book);

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(3)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getInt(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getDouble(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        book = bookDao.getBookByISBN("test");
        assertEquals(Optional.empty(), book);

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBookByISBN("test"));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> bookDao.getBookByISBN("test"));
    }
}