package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.utility.page.Page;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCPlaceDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private CallableStatement callableStatement;
    @Mock
    private ResultSet resultSet;
    private PlaceDao placeDao;

    @Test
    void create() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(1)).thenReturn(1L);
        placeDao = new JDBCPlaceDao(connection);
        Place place = Place.builder()
                .setName("name")
                .createPlace();
        assertEquals(1L, placeDao.create(place));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection).prepareStatement(Mockito.anyString(), Mockito.anyInt());
        Mockito.verify(preparedStatement).setString(1, "name");
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(preparedStatement).getGeneratedKeys();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong(1);
        Mockito.verify(connection).commit();

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(0L, placeDao.create(place));

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(DaoException.class, () -> placeDao.create(place));

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString(), Mockito.anyInt());
        assertThrows(DaoException.class, () -> placeDao.create(place));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> placeDao.create(place));
    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        placeDao = new JDBCPlaceDao(connection);
        Place expected = Place.builder()
                .setId(1L)
                .setName("name")
                .createPlace();
        assertEquals(Optional.of(expected), placeDao.getById(1L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(1, 1L);
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong("id");
        Mockito.verify(resultSet).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), placeDao.getById(1L));

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(DaoException.class, () -> placeDao.getById(1L));

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString());
        assertThrows(DaoException.class, () -> placeDao.getById(1L));
    }

    @Test
    void getPage() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        Mockito.when(callableStatement.getLong(5)).thenReturn(1L);

        placeDao = new JDBCPlaceDao(connection);
        Place expected = Place.builder()
                .setId(1L)
                .setName("name")
                .createPlace();
        Page<Place> expectedPage = Page.<Place>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .setElementsCount(1)
                .setData(List.of(expected))
                .createPage();
        Page<Place> page = Page.<Place>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        assertEquals(expectedPage, placeDao.getPage(page));

        Mockito.verify(connection).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet).getLong("id");
        Mockito.verify(resultSet).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        expectedPage.setElementsCount(0);
        expectedPage.setData(List.of());
        Mockito.when(callableStatement.getLong(5)).thenReturn(0L);
        assertEquals(expectedPage, placeDao.getPage(page));

        Mockito.doThrow(SQLException.class).when(callableStatement).executeQuery();
        assertThrows(DaoException.class, () -> placeDao.getPage(page));

        Mockito.doThrow(SQLException.class).when(connection).prepareCall(Mockito.anyString());
        assertThrows(DaoException.class, () -> placeDao.getPage(page));
    }

    @Test
    void update() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        placeDao = new JDBCPlaceDao(connection);
        Place place = Place.builder()
                .setId(1L)
                .setName("name")
                .createPlace();
        assertDoesNotThrow(() -> placeDao.update(place));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(1, "name");
        Mockito.verify(preparedStatement).setLong(2, 1L);
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(connection).commit();

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(DaoException.class, () -> placeDao.update(place));

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString());
        assertThrows(DaoException.class, () -> placeDao.update(place));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> placeDao.update(place));
    }

    @Test
    void delete() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        placeDao = new JDBCPlaceDao(connection);
        assertDoesNotThrow(() -> placeDao.delete(1L));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(1, 1L);
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(connection).commit();

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(DaoException.class, () -> placeDao.delete(1L));

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString());
        assertThrows(DaoException.class, () -> placeDao.delete(1L));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> placeDao.delete(1L));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        placeDao = new JDBCPlaceDao(connection);
        assertDoesNotThrow(() -> placeDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> placeDao.close());
    }

    @Test
    void getByOrderId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        placeDao = new JDBCPlaceDao(connection);
        Place expected = Place.builder()
                .setId(1L)
                .setName("name")
                .createPlace();
        assertEquals(Optional.of(expected), placeDao.getByOrderId(1L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(1, 1L);
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong("id");
        Mockito.verify(resultSet).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), placeDao.getByOrderId(1L));

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(DaoException.class, () -> placeDao.getByOrderId(1L));

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString());
        assertThrows(DaoException.class, () -> placeDao.getByOrderId(1L));
    }

    @Test
    void getByName() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        placeDao = new JDBCPlaceDao(connection);
        Place expected = Place.builder()
                .setId(1L)
                .setName("name")
                .createPlace();
        assertEquals(Optional.of(expected), placeDao.getByName("name"));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(1, "name");
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getLong("id");
        Mockito.verify(resultSet).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), placeDao.getByName("name"));

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(DaoException.class, () -> placeDao.getByName("name"));

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString());
        assertThrows(DaoException.class, () -> placeDao.getByName("name"));
    }

    @Test
    void getAll() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        placeDao = new JDBCPlaceDao(connection);
        List<Place> expected = List.of(
                Place.builder()
                        .setId(1L)
                        .setName("name")
                        .createPlace(),
                Place.builder()
                        .setId(1L)
                        .setName("name")
                        .createPlace()
        );
        assertEquals(expected, placeDao.getAll());

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(2)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(List.of(), placeDao.getAll());

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(DaoException.class, () -> placeDao.getAll());

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString());
        assertThrows(DaoException.class, () -> placeDao.getAll());
    }
}