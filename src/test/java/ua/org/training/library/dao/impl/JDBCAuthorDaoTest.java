package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Author;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCAuthorDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private CallableStatement callableStatement;
    @Mock
    private ResultSet resultSet;
    private AuthorDao authorDao;

    @Test
    void getAuthorsByBookId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.doNothing().when(preparedStatement).close();
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("test");
        Mockito.doNothing().when(resultSet).close();
        authorDao = new JDBCAuthorDao(connection);
        List<Author> authors = authorDao.getAuthorsByBookId(1L);
        assertEquals(2, authors.size());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(1)).close();
        Mockito.verify(preparedStatement, Mockito.times(1)).close();

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getAuthorsByBookId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getAuthorsByBookId(1L));
    }

    @Test
    void getAuthorsByBookIsbn() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.doNothing().when(preparedStatement).close();
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("test");
        Mockito.doNothing().when(resultSet).close();
        authorDao = new JDBCAuthorDao(connection);
        Author author = new Author(1L, "test", "test");
        assertEquals(List.of(author), authorDao.getAuthorsByBookIsbn("test"));
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).close();
        Mockito.verify(preparedStatement, Mockito.times(1)).close();

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getAuthorsByBookIsbn("1234567890123"));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getAuthorsByBookIsbn("1234567890123"));
    }

    @Test
    void create() throws SQLException {
        Mockito.doNothing().when(connection).setAutoCommit(Mockito.anyBoolean());
        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyInt())).thenReturn(1L);
        Mockito.doNothing().when(resultSet).close();
        Mockito.doNothing().when(preparedStatement).close();
        authorDao = new JDBCAuthorDao(connection);
        Author author = Author.builder()
                .setFirstName("firstName")
                .setLastName("lastName")
                .createAuthor();
        assertEquals(1L, authorDao.create(author));
        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString(), Mockito.anyInt());
        Mockito.verify(preparedStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(preparedStatement, Mockito.times(1)).getGeneratedKeys();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyInt());
        Mockito.verify(resultSet, Mockito.times(1)).close();
        Mockito.verify(preparedStatement, Mockito.times(1)).close();

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(0, authorDao.create(author));

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.create(author));

        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.create(author));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> authorDao.create(author));
    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString("first_name")).thenReturn("firstName");
        Mockito.when(resultSet.getString("last_name")).thenReturn("lastName");
        Mockito.doNothing().when(resultSet).close();
        Mockito.doNothing().when(preparedStatement).close();
        authorDao = new JDBCAuthorDao(connection);
        Author author = Author.builder()
                .setId(1L)
                .setFirstName("firstName")
                .setLastName("lastName")
                .createAuthor();
        assertEquals(Optional.of(author), authorDao.getById(1L));
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).close();
        Mockito.verify(preparedStatement, Mockito.times(1)).close();

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), authorDao.getById(1L));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getById(1L));
    }

    @Test
    void getPage() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.doNothing().when(callableStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.doNothing().when(callableStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString("first_name")).thenReturn("firstName");
        Mockito.when(resultSet.getString("last_name")).thenReturn("lastName");
        Mockito.doNothing().when(resultSet).close();
        Mockito.doNothing().when(callableStatement).close();
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet1);
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getLong(Mockito.anyInt())).thenReturn(1L);
        authorDao = new JDBCAuthorDao(connection);
        Author author = Author.builder()
                .setId(1L)
                .setFirstName("firstName")
                .setLastName("lastName")
                .createAuthor();
        Page<Author> page = Page.<Author>builder()
                .setSearch("")
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .createPage();
        Page<Author> resultingPage = Page.<Author>builder()
                .setSearch("")
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(author))
                .createPage();
        assertEquals(resultingPage, authorDao.getPage(page));
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet1, Mockito.times(1)).next();
        Mockito.verify(resultSet1, Mockito.times(1)).getLong(Mockito.anyInt());
        Mockito.verify(resultSet1, Mockito.times(1)).close();
        Mockito.verify(preparedStatement, Mockito.times(1)).close();
        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).close();
        Mockito.verify(callableStatement, Mockito.times(1)).close();

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getPage(page));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getPage(page));

        Mockito.when(callableStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getPage(page));

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.getPage(page));
    }

    @Test
    void update() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.doNothing().when(preparedStatement).close();
        authorDao = new JDBCAuthorDao(connection);
        Author author = Author.builder()
                .setId(1L)
                .setFirstName("firstName")
                .setLastName("lastName")
                .createAuthor();
        assertDoesNotThrow(() -> authorDao.update(author));
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(preparedStatement, Mockito.times(1)).close();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.update(author));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.update(author));
    }

    @Test
    void delete() throws SQLException {
        Mockito.doNothing().when(connection).setAutoCommit(Mockito.anyBoolean());
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.doNothing().when(preparedStatement).close();
        Mockito.doNothing().when(connection).commit();
        authorDao = new JDBCAuthorDao(connection);
        assertDoesNotThrow(() -> authorDao.delete(1L));
        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(2)).executeUpdate();
        Mockito.verify(preparedStatement, Mockito.times(2)).close();
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.doThrow(SQLException.class).when(connection).commit();
        assertThrows(DaoException.class, () -> authorDao.delete(1L));

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.delete(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> authorDao.delete(1L));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> authorDao.delete(1L));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        authorDao = new JDBCAuthorDao(connection);
        assertDoesNotThrow(() -> authorDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> authorDao.close());
    }
}