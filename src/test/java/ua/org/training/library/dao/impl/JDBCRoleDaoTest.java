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
class JDBCRoleDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private RoleDao roleDao;

    @Test
    void create() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(1)).thenReturn(1L);
        roleDao = new JDBCRoleDao(connection);
        Role role = Role.builder()
                .setCode("code")
                .setName("name")
                .createRole();
        long id = roleDao.create(role);
        assertEquals(1, id);

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString(), Mockito.anyInt());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(1, "code");
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(2, "name");
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(preparedStatement, Mockito.times(1)).getGeneratedKeys();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(1);
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(resultSet.next()).thenReturn(false);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        assertEquals(0, roleDao.create(role));

        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.create(role));

        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.create(role));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> roleDao.create(role));
    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("code")).thenReturn("code");
        Mockito.when(resultSet.getString("name")).thenReturn("name");

        roleDao = new JDBCRoleDao(connection);
        Role expected = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        assertEquals(Optional.of(expected), roleDao.getById(1L));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(1, 1L);
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("code");
        Mockito.verify(resultSet, Mockito.times(1)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), roleDao.getById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getById(1L));
    }

    @Test
    void getPage() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("code")).thenReturn("code");
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        roleDao = new JDBCRoleDao(connection);
        Role expected = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        Page<Role> expectedPage = Page.<Role>builder()
                .setSearch("")
                .setSorting("ASC")
                .setPageNumber(0)
                .setLimit(5)
                .setElementsCount(1)
                .setData(List.of(expected))
                .createPage();
        Page<Role> page = Page.<Role>builder()
                .setSearch("")
                .setSorting("ASC")
                .setPageNumber(0)
                .setLimit(5)
                .createPage();
        assertEquals(expectedPage, roleDao.getPage(page));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("code");
        Mockito.verify(resultSet, Mockito.times(1)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        expectedPage.setElementsCount(0);
        expectedPage.setData(List.of());
        assertEquals(expectedPage, roleDao.getPage(page));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getPage(page));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getPage(page));
    }

    @Test
    void update() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        roleDao = new JDBCRoleDao(connection);
        Role role = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        assertDoesNotThrow(() -> roleDao.update(role));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(1, "code");
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(2, "name");
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(3, 1L);
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.update(role));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.update(role));
    }

    @Test
    void delete() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        roleDao = new JDBCRoleDao(connection);
        assertDoesNotThrow(() -> roleDao.delete(1L));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setLong(1, 1L);
        Mockito.verify(preparedStatement, Mockito.times(2)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.delete(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.delete(1L));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        roleDao = new JDBCRoleDao(connection);
        assertDoesNotThrow(() -> roleDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> roleDao.close());
    }

    @Test
    void getByCode() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("code")).thenReturn("code");
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        roleDao = new JDBCRoleDao(connection);
        Role expected = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        assertEquals(Optional.of(expected), roleDao.getByCode("code"));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(1, "code");
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("code");
        Mockito.verify(resultSet, Mockito.times(1)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), roleDao.getByCode("code"));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getByCode("code"));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getByCode("code"));
    }

    @Test
    void getByName() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("code")).thenReturn("code");
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        roleDao = new JDBCRoleDao(connection);
        Role expected = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        assertEquals(Optional.of(expected), roleDao.getByName("name"));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(1, "name");
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("code");
        Mockito.verify(resultSet, Mockito.times(1)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), roleDao.getByName("name"));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getByName("name"));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getByName("name"));
    }

    @Test
    void getRolesByUserId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("code")).thenReturn("code");
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        roleDao = new JDBCRoleDao(connection);
        Role expected = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        List<Role> expectedList = List.of(
                expected
        );
        assertEquals(expectedList, roleDao.getRolesByUserId(1L));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(1, 1L);
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("code");
        Mockito.verify(resultSet, Mockito.times(1)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(List.of(), roleDao.getRolesByUserId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getRolesByUserId(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getRolesByUserId(1L));
    }

    @Test
    void getRolesByUserLogin() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("code")).thenReturn("code");
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        roleDao = new JDBCRoleDao(connection);
        Role expected = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        List<Role> expectedList = List.of(
                expected
        );
        assertEquals(expectedList, roleDao.getRolesByUserLogin("login"));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setString(1, "login");
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("code");
        Mockito.verify(resultSet, Mockito.times(1)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(List.of(), roleDao.getRolesByUserLogin("login"));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getRolesByUserLogin("login"));

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getRolesByUserLogin("login"));
    }

    @Test
    void getAllRoles() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("code")).thenReturn("code");
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        roleDao = new JDBCRoleDao(connection);
        Role expected = Role.builder()
                .setId(1L)
                .setCode("code")
                .setName("name")
                .createRole();
        List<Role> expectedList = List.of(
                expected
        );
        assertEquals(expectedList, roleDao.getAllRoles());

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getString("code");
        Mockito.verify(resultSet, Mockito.times(1)).getString("name");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(List.of(), roleDao.getAllRoles());

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getAllRoles());

        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> roleDao.getAllRoles());
    }
}