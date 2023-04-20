package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCHistoryOrderDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private CallableStatement callableStatement;
    @Mock
    private ResultSet resultSet;
    private HistoryOrderDao historyOrderDao;

    @Test
    void create() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(1)).thenReturn(1L);
        historyOrderDao = new JDBCHistoryOrderDao(connection);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setBookName("bookName")
                .setUser(User.builder().setId(1L).createUser())
                .setDateCreated(new Date(1))
                .setStatus(Status.builder().setId(1).createStatus())
                .setDateExpire(new Date(1))
                .createHistoryOrder();

        assertEquals(1L, historyOrderDao.create(historyOrder));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).commit();
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(4, "bookName");
        Mockito.verify(preparedStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(2)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(preparedStatement, Mockito.times(1)).getGeneratedKeys();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(1);

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(0L, historyOrderDao.create(historyOrder));
        Mockito.verify(connection, Mockito.times(1)).rollback();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.create(historyOrder));
    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("bookName");
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        historyOrderDao = new JDBCHistoryOrderDao(connection);

        HistoryOrder historyOrder = HistoryOrder.builder()
                .setId(1L)
                .setBookName("bookName")
                .setDateExpire(new Date(1))
                .setDateCreated(new Date(1))
                .createHistoryOrder();
        assertEquals(Optional.of(historyOrder), historyOrderDao.getById(1L));

        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(1, 1L);
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("date_expire");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), historyOrderDao.getById(1L));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.getById(1L));
    }

    @Test
    void getPage() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("bookName");
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        Mockito.when(callableStatement.getLong(5)).thenReturn(2L);

        historyOrderDao = new JDBCHistoryOrderDao(connection);
        Page<HistoryOrder> page = Page.<HistoryOrder>builder()
                .setPageNumber(0)
                .setLimit(2)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Page<HistoryOrder> expectedPage = Page.<HistoryOrder>builder()
                .setPageNumber(0)
                .setLimit(2)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(2L)
                .setData(Arrays.asList(
                        HistoryOrder.builder()
                                .setId(1L)
                                .setBookName("bookName")
                                .setDateExpire(new Timestamp(1))
                                .setDateCreated(new Timestamp(1))
                                .createHistoryOrder(),
                        HistoryOrder.builder()
                                .setId(1L)
                                .setBookName("bookName")
                                .setDateExpire(new Timestamp(1))
                                .setDateCreated(new Timestamp(1))
                                .createHistoryOrder()
                ))
                .createPage();
        assertEquals(expectedPage, historyOrderDao.getPage(page));

        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(2)).getString("book_name");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_expire");

        Mockito.when(resultSet.next()).thenReturn(false);
        expectedPage = Page.<HistoryOrder>builder()
                .setPageNumber(0)
                .setLimit(2)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(2L)
                .setData(Collections.emptyList())
                .createPage();
        assertEquals(expectedPage, historyOrderDao.getPage(page));

        Mockito.when(callableStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.getPage(page));
    }

    @Test
    void update() throws SQLException {
        assertThrows(NullPointerException.class, () -> historyOrderDao.update(null));
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setId(1L)
                .setBookName("bookName")
                .setUser(User.builder().setId(1L).createUser())
                .setStatus(Status.builder().setId(1L).createStatus())
                .setDateExpire(new Date(1))
                .setDateCreated(new Date(1))
                .createHistoryOrder();
        historyOrderDao = new JDBCHistoryOrderDao(connection);
        assertDoesNotThrow(() -> historyOrderDao.update(historyOrder));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.update(historyOrder));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.update(historyOrder));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> historyOrderDao.update(historyOrder));
    }

    @Test
    void delete() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        historyOrderDao = new JDBCHistoryOrderDao(connection);
        assertDoesNotThrow(() -> historyOrderDao.delete(1L));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.delete(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.delete(1L));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> historyOrderDao.delete(1L));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        historyOrderDao = new JDBCHistoryOrderDao(connection);
        assertDoesNotThrow(() -> historyOrderDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> historyOrderDao.close());
    }

    @Test
    void getPageByUserId() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("book_name")).thenReturn("bookName");
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(callableStatement.getLong(6)).thenReturn(2L);

        historyOrderDao = new JDBCHistoryOrderDao(connection);
        Page<HistoryOrder> page = Page.<HistoryOrder>builder()
                .setPageNumber(0)
                .setSorting("ASC")
                .setLimit(5)
                .setSearch("")
                .createPage();
        Page<HistoryOrder> expectedPage = Page.<HistoryOrder>builder()
                .setPageNumber(0)
                .setSorting("ASC")
                .setLimit(5)
                .setSearch("")
                .setData(Arrays.asList(
                        HistoryOrder.builder()
                                .setId(1L)
                                .setBookName("bookName")
                                .setDateExpire(new Timestamp(1))
                                .setDateCreated(new Timestamp(1))
                                .createHistoryOrder(),
                        HistoryOrder.builder()
                                .setId(1L)
                                .setBookName("bookName")
                                .setDateExpire(new Timestamp(1))
                                .setDateCreated(new Timestamp(1))
                                .createHistoryOrder()
                ))
                .setElementsCount(2L)
                .createPage();
        assertEquals(expectedPage, historyOrderDao.getPageByUserId(page, 1L));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(4)).getTimestamp(Mockito.anyString());

        Mockito.when(callableStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.getPageByUserId(page, 1L));

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> historyOrderDao.getPageByUserId(page, 1L));

        Mockito.doThrow(SQLException.class).when(connection).prepareCall(Mockito.anyString());
        assertThrows(DaoException.class, () -> historyOrderDao.getPageByUserId(page, 1L));
    }
}