package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCStatusDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private StatusDao statusDao;

    @Test
    void create() throws SQLException {
        statusDao = new JDBCStatusDao(connection);
        assertThrows(UnsupportedOperationException.class, () -> statusDao.create(null));
    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("status");
        Mockito.when(resultSet.getBoolean(Mockito.anyString())).thenReturn(true);
        statusDao = new JDBCStatusDao(connection);
        Status expected = Status.builder()
                .setId(1L)
                .setName("status")
                .setCode("status")
                .setClosed(false)
                .createStatus();
        assertEquals(Optional.of(expected), statusDao.getById(1L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString(Mockito.anyString());
        Mockito.verify(resultSet).getBoolean(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), statusDao.getById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getById(1L));
    }

    @Test
    void getPage() {
        statusDao = new JDBCStatusDao(connection);
        assertThrows(UnsupportedOperationException.class, () -> statusDao.getPage(null));
    }

    @Test
    void update() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        statusDao = new JDBCStatusDao(connection);
        Status status = Status.builder()
                .setId(1L)
                .setName("status")
                .setCode("status")
                .setClosed(false)
                .createStatus();
        assertDoesNotThrow(() -> statusDao.update(status));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).setBoolean(Mockito.anyInt(), Mockito.anyBoolean());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeUpdate();

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.update(status));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.update(status));
    }

    @Test
    void delete() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        statusDao = new JDBCStatusDao(connection);
        assertDoesNotThrow(() -> statusDao.delete(1L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeUpdate();

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.delete(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.delete(1L));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        statusDao = new JDBCStatusDao(connection);
        assertDoesNotThrow(() -> statusDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> statusDao.close());
    }

    @Test
    void getByCode() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("status");
        Mockito.when(resultSet.getBoolean(Mockito.anyString())).thenReturn(true);
        statusDao = new JDBCStatusDao(connection);
        Status expected = Status.builder()
                .setId(1L)
                .setName("status")
                .setCode("status")
                .setClosed(false)
                .createStatus();
        assertEquals(Optional.of(expected), statusDao.getByCode("status"));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString(Mockito.anyString());
        Mockito.verify(resultSet).getBoolean(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), statusDao.getByCode("status"));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getByCode("status"));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getByCode("status"));
    }

    @Test
    void getByOrderId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("status");
        Mockito.when(resultSet.getBoolean(Mockito.anyString())).thenReturn(true);
        statusDao = new JDBCStatusDao(connection);
        Status expected = Status.builder()
                .setId(1L)
                .setName("status")
                .setCode("status")
                .setClosed(false)
                .createStatus();
        assertEquals(Optional.of(expected), statusDao.getByOrderId(1L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString(Mockito.anyString());
        Mockito.verify(resultSet).getBoolean(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), statusDao.getByOrderId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getByOrderId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getByOrderId(1L));
    }

    @Test
    void getByHistoryOrderId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("status");
        Mockito.when(resultSet.getBoolean(Mockito.anyString())).thenReturn(true);
        statusDao = new JDBCStatusDao(connection);
        Status expected = Status.builder()
                .setId(1L)
                .setName("status")
                .setCode("status")
                .setClosed(false)
                .createStatus();
        assertEquals(Optional.of(expected), statusDao.getByHistoryOrderId(1L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString(Mockito.anyString());
        Mockito.verify(resultSet).getBoolean(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), statusDao.getByHistoryOrderId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getByHistoryOrderId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getByHistoryOrderId(1L));
    }

    @Test
    void getNextStatusesForStatusById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("status");
        Mockito.when(resultSet.getBoolean(Mockito.anyString())).thenReturn(true);
        statusDao = new JDBCStatusDao(connection);
        Status expected = Status.builder()
                .setId(1L)
                .setName("status")
                .setCode("status")
                .setClosed(false)
                .createStatus();
        List<Status> expectedList = List.of(
                expected,
                expected
        );
        assertEquals(expectedList, statusDao.getNextStatusesForStatusById(1L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(4)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getBoolean(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(List.of(), statusDao.getNextStatusesForStatusById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getNextStatusesForStatusById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> statusDao.getNextStatusesForStatusById(1L));
    }
}