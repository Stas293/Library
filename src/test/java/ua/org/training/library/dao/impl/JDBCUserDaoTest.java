package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCUserDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private CallableStatement callableStatement;
    @Mock
    private ResultSet resultSet;
    private UserDao userDao;

    @Test
    void create() throws SQLException {
        userDao = new JDBCUserDao(connection);
        assertEquals(Constants.APP_DEFAULT_ID, userDao.create(new User()));
    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("login");
        Mockito.when(resultSet.getString("first_name")).thenReturn("first_name");
        Mockito.when(resultSet.getString("last_name")).thenReturn("last_name");
        Mockito.when(resultSet.getString("email")).thenReturn("email");
        Mockito.when(resultSet.getString("phone")).thenReturn("phone");
        Mockito.when(resultSet.getInt("enabled")).thenReturn(1);
        Mockito.when(resultSet.getDate("date_created")).thenReturn(Date.valueOf("2020-01-01"));
        Mockito.when(resultSet.getDate("date_updated")).thenReturn(Date.valueOf("2020-01-01"));
        userDao = new JDBCUserDao(connection);
        User expected = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("first_name")
                .setLastName("last_name")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2020-01-01"))
                .setDateUpdated(Date.valueOf("2020-01-01"))
                .createUser();
        assertEquals(Optional.of(expected), userDao.getById(1L));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(1, 1L);
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getString("login");
        Mockito.verify(resultSet, Mockito.times(1)).getString("first_name");
        Mockito.verify(resultSet, Mockito.times(1)).getString("last_name");
        Mockito.verify(resultSet, Mockito.times(1)).getString("email");
        Mockito.verify(resultSet, Mockito.times(1)).getString("phone");
        Mockito.verify(resultSet, Mockito.times(1)).getInt("enabled");
        Mockito.verify(resultSet, Mockito.times(1)).getDate("date_created");
        Mockito.verify(resultSet, Mockito.times(1)).getDate("date_updated");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), userDao.getById(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.getById(1L));
    }

    @Test
    void getPage() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("login");
        Mockito.when(resultSet.getString("first_name")).thenReturn("first_name");
        Mockito.when(resultSet.getString("last_name")).thenReturn("last_name");
        Mockito.when(resultSet.getString("email")).thenReturn("email");
        Mockito.when(resultSet.getString("phone")).thenReturn("phone");
        Mockito.when(resultSet.getInt("enabled")).thenReturn(1);
        Mockito.when(resultSet.getDate("date_created")).thenReturn(Date.valueOf("2020-01-01"));
        Mockito.when(resultSet.getDate("date_updated")).thenReturn(Date.valueOf("2020-01-01"));
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet1);
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getLong(1)).thenReturn(2L);
        userDao = new JDBCUserDao(connection);
        List<User> expected = List.of(
                User.builder()
                        .setId(1L)
                        .setLogin("login")
                        .setFirstName("first_name")
                        .setLastName("last_name")
                        .setEmail("email")
                        .setPhone("phone")
                        .setEnabled(true)
                        .setDateCreated(Date.valueOf("2020-01-01"))
                        .setDateUpdated(Date.valueOf("2020-01-01"))
                        .createUser(),
                User.builder()
                        .setId(1L)
                        .setLogin("login")
                        .setFirstName("first_name")
                        .setLastName("last_name")
                        .setEmail("email")
                        .setPhone("phone")
                        .setEnabled(true)
                        .setDateCreated(Date.valueOf("2020-01-01"))
                        .setDateUpdated(Date.valueOf("2020-01-01"))
                        .createUser()
        );
        Page<User> page = Page.<User>builder()
                .setPageNumber(0)
                .setSorting("ASC")
                .setLimit(5)
                .setSearch("")
                .createPage();
        Page<User> expectedPage = Page.<User>builder()
                .setPageNumber(0)
                .setSorting("ASC")
                .setLimit(5)
                .setSearch("")
                .setData(expected)
                .setElementsCount(2)
                .createPage();
        assertEquals(expectedPage, userDao.getPage(page));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getString("login");
        Mockito.verify(resultSet, Mockito.times(2)).getString("first_name");
        Mockito.verify(resultSet, Mockito.times(2)).getString("last_name");
        Mockito.verify(resultSet, Mockito.times(2)).getString("email");
        Mockito.verify(resultSet, Mockito.times(2)).getString("phone");
        Mockito.verify(resultSet, Mockito.times(2)).getInt("enabled");
        Mockito.verify(resultSet, Mockito.times(2)).getDate("date_created");
        Mockito.verify(resultSet, Mockito.times(2)).getDate("date_updated");

        Mockito.when(resultSet.next()).thenReturn(false);
        expectedPage.setElementsCount(0);
        expectedPage.setData(List.of());
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getLong(1)).thenReturn(0L);
        assertEquals(expectedPage, userDao.getPage(page));

        Mockito.when(connection.prepareCall(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.getPage(page));
    }

    @Test
    void update() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Field DELETE_ROLES_BY_USER_ID = JDBCUserDao.class.getDeclaredField("DELETE_ROLES_BY_USER_ID");
        DELETE_ROLES_BY_USER_ID.setAccessible(true);
        Mockito.when(connection.prepareStatement(DELETE_ROLES_BY_USER_ID.get("String").toString())).thenReturn(preparedStatement);
        Field INSERT_USER_ROLE = JDBCUserDao.class.getDeclaredField("INSERT_USER_ROLE");
        INSERT_USER_ROLE.setAccessible(true);
        PreparedStatement preparedStatement1 = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(INSERT_USER_ROLE.get("String").toString())).thenReturn(preparedStatement1);
        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("first_name")
                .setLastName("last_name")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2020-01-01"))
                .setDateUpdated(Date.valueOf("2020-01-01"))
                .setRoles(List.of(
                        Role.builder()
                                .setId(1L)
                                .setName("name")
                                .createRole()
                        )
                )
                .createUser();
        assertDoesNotThrow(() -> userDao.update(user));
        Mockito.verify(connection, Mockito.times(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement1, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(connection.prepareStatement(INSERT_USER_ROLE.get("String").toString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.update(user));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(DaoException.class, () -> userDao.update(user));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.update(user));
    }

    @Test
    void delete() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        userDao = new JDBCUserDao(connection);
        assertDoesNotThrow(() -> userDao.delete(1L));
        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(2)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(DaoException.class, () -> userDao.delete(1L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.delete(1L));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> userDao.delete(1L));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        userDao = new JDBCUserDao(connection);
        assertDoesNotThrow(() -> userDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> userDao.close());
    }

    @Test
    void testCreate() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Field INSERT_USER = JDBCUserDao.class.getDeclaredField("INSERT_USER");
        INSERT_USER.setAccessible(true);
        Mockito.when(connection.prepareCall(INSERT_USER.get("String").toString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeUpdate()).thenReturn(1);
        Mockito.doNothing().when(callableStatement).registerOutParameter(Mockito.anyInt(), Mockito.anyInt());
        Mockito.when(callableStatement.getLong(8)).thenReturn(1L);
        Field INSERT_USER_ROLE = JDBCUserDao.class.getDeclaredField("INSERT_USER_ROLE");
        INSERT_USER_ROLE.setAccessible(true);
        Mockito.when(connection.prepareStatement(INSERT_USER_ROLE.get("String").toString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setRoles(
                        List.of(
                                Role.builder().setId(1L).setName("role1").setCode("code1").createRole(),
                                Role.builder().setId(2L).setName("role2").setCode("code2").createRole()
                        )
                )
                .createUser();
        assertEquals(1L, userDao.create(user, "password"));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection).prepareCall(INSERT_USER.get("String").toString());
        Mockito.verify(callableStatement, Mockito.times(6)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement).setBoolean(7, true);
        Mockito.verify(callableStatement).executeUpdate();
        Mockito.verify(callableStatement).registerOutParameter(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(callableStatement).getLong(8);
        Mockito.verify(connection, Mockito.times(2)).prepareStatement(INSERT_USER_ROLE.get("String").toString());
        Mockito.verify(preparedStatement, Mockito.times(4)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(2)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.create(user, "password"));

        Mockito.when(callableStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.create(user, "password"));

        Mockito.when(connection.prepareCall(INSERT_USER.get("String").toString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.create(user, "password"));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(false);
        assertThrows(SQLException.class, () -> userDao.create(user, "password"));
    }

    @Test
    void testUpdate() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setRoles(
                        List.of(
                                Role.builder().setId(1L).setName("role1").setCode("code1").createRole(),
                                Role.builder().setId(2L).setName("role2").setCode("code2").createRole()
                        )
                )
                .createUser();
        assertDoesNotThrow(() -> userDao.update(user, "password"));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1))
                .setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1))
                .setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(connection).commit();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.update(user, "password"));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.update(user, "password"));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(false);
        assertThrows(SQLException.class, () -> userDao.update(user, "password"));
    }

    @Test
    void getByLogin() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("login");
        Mockito.when(resultSet.getString("first_name")).thenReturn("firstName");
        Mockito.when(resultSet.getString("last_name")).thenReturn("lastName");
        Mockito.when(resultSet.getString("email")).thenReturn("email");
        Mockito.when(resultSet.getString("phone")).thenReturn("phone");
        Mockito.when(resultSet.getInt("enabled")).thenReturn(1);
        Mockito.when(resultSet.getDate("date_created")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getDate("date_updated")).thenReturn(Date.valueOf("2021-01-01"));

        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2021-01-01"))
                .setDateUpdated(Date.valueOf("2021-01-01"))
                .createUser();

        assertEquals(Optional.of(user), userDao.getByLogin("login"));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(5)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getInt(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getDate(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), userDao.getByLogin("login"));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.getByLogin("login"));
    }

    @Test
    void getByEmail() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("login");
        Mockito.when(resultSet.getString("first_name")).thenReturn("firstName");
        Mockito.when(resultSet.getString("last_name")).thenReturn("lastName");
        Mockito.when(resultSet.getString("email")).thenReturn("email");
        Mockito.when(resultSet.getString("phone")).thenReturn("phone");
        Mockito.when(resultSet.getInt("enabled")).thenReturn(1);
        Mockito.when(resultSet.getDate("date_created")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getDate("date_updated")).thenReturn(Date.valueOf("2021-01-01"));

        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2021-01-01"))
                .setDateUpdated(Date.valueOf("2021-01-01"))
                .createUser();

        assertEquals(Optional.of(user), userDao.getByEmail("email"));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(5)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getInt(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getDate(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), userDao.getByEmail("email"));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.getByEmail("email"));
    }

    @Test
    void getByPhone() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("login");
        Mockito.when(resultSet.getString("first_name")).thenReturn("firstName");
        Mockito.when(resultSet.getString("last_name")).thenReturn("lastName");
        Mockito.when(resultSet.getString("email")).thenReturn("email");
        Mockito.when(resultSet.getString("phone")).thenReturn("phone");
        Mockito.when(resultSet.getInt("enabled")).thenReturn(1);
        Mockito.when(resultSet.getDate("date_created")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getDate("date_updated")).thenReturn(Date.valueOf("2021-01-01"));

        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2021-01-01"))
                .setDateUpdated(Date.valueOf("2021-01-01"))
                .createUser();

        assertEquals(Optional.of(user), userDao.getByPhone("phone"));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(5)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getInt(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getDate(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), userDao.getByPhone("phone"));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.getByPhone("phone"));
    }

    @Test
    void getByOrderId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("login");
        Mockito.when(resultSet.getString("first_name")).thenReturn("firstName");
        Mockito.when(resultSet.getString("last_name")).thenReturn("lastName");
        Mockito.when(resultSet.getString("email")).thenReturn("email");
        Mockito.when(resultSet.getString("phone")).thenReturn("phone");
        Mockito.when(resultSet.getInt("enabled")).thenReturn(1);
        Mockito.when(resultSet.getDate("date_created")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getDate("date_updated")).thenReturn(Date.valueOf("2021-01-01"));

        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2021-01-01"))
                .setDateUpdated(Date.valueOf("2021-01-01"))
                .createUser();

        assertEquals(Optional.of(user), userDao.getByOrderId(0L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(5)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getInt(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getDate(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), userDao.getByOrderId(0L));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.getByOrderId(0L));
    }

    @Test
    void getByHistoryOrderId() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("login");
        Mockito.when(resultSet.getString("first_name")).thenReturn("firstName");
        Mockito.when(resultSet.getString("last_name")).thenReturn("lastName");
        Mockito.when(resultSet.getString("email")).thenReturn("email");
        Mockito.when(resultSet.getString("phone")).thenReturn("phone");
        Mockito.when(resultSet.getInt("enabled")).thenReturn(1);
        Mockito.when(resultSet.getDate("date_created")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getDate("date_updated")).thenReturn(Date.valueOf("2021-01-01"));

        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2021-01-01"))
                .setDateUpdated(Date.valueOf("2021-01-01"))
                .createUser();

        assertEquals(Optional.of(user), userDao.getByHistoryOrderId(0L));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(5)).getString(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(1)).getInt(Mockito.anyString());
        Mockito.verify(resultSet, Mockito.times(2)).getDate(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), userDao.getByHistoryOrderId(0L));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.getByHistoryOrderId(0L));
    }

    @Test
    void disable() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);

        userDao = new JDBCUserDao(connection);
        assertDoesNotThrow(() -> userDao.disable(0L));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(connection).commit();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.disable(0L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.disable(0L));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> userDao.disable(0L));
    }

    @Test
    void enable() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        userDao = new JDBCUserDao(connection);
        assertDoesNotThrow(() -> userDao.enable(0L));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(connection).commit();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.enable(0L));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.enable(0L));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> userDao.enable(0L));
    }

    @Test
    void updateData() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        userDao = new JDBCUserDao(connection);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(Date.valueOf("2021-01-01"))
                .setDateUpdated(Date.valueOf("2021-01-01"))
                .createUser();
        assertDoesNotThrow(() -> userDao.updateData(user));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(5)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement).executeUpdate();
        Mockito.verify(connection).commit();

        Mockito.when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.updateData(user));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> userDao.updateData(user));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> userDao.updateData(user));
    }
}