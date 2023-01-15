package ua.org.training.library.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.Status;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private UserDao userDao;
    @Mock
    private RoleDao roleDao;
    private UserService userService;

    @Test
    void createUser() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setRoles(List.of(Role.builder().setId(1L).createRole()))
                .createUser();
        Mockito.when(userDao.create(user, "password")).thenReturn(1L);
        assertEquals(1L, userService.createUser(user, "password"));

        Mockito.when(userDao.create(user, "password")).thenThrow(SQLException.class);
        assertThrows(ConnectionDBException.class, () -> userService.createUser(user, "password"));

        userDao = Mockito.mock(UserDao.class);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(userDao.create(user, "password")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.createUser(user, "password"));

        Mockito.when(daoFactory.createUserDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> userService.createUser(user, "password"));
    }

    @Test
    void getUserById() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        Mockito.when(userDao.getById(1L)).thenReturn(Optional.ofNullable(user));
        Mockito.when(roleDao.getRolesByUserId(1L)).thenReturn(List.of(Role.builder().setId(1L).createRole()));
        User expectedUser = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .setRoles(List.of(Role.builder().setId(1L).createRole()))
                .createUser();
        assertEquals(expectedUser, userService.getUserById(1L));

        Mockito.when(userDao.getById(1L)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.getUserById(1L));

        Mockito.when(daoFactory.createUserDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getUserByLogin() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        Mockito.when(userDao.getByLogin("login")).thenReturn(Optional.ofNullable(user));
        Mockito.when(roleDao.getRolesByUserId(1L)).thenReturn(List.of(Role.builder().setId(1L).createRole()));
        User expectedUser = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .setRoles(List.of(Role.builder().setId(1L).createRole()))
                .createUser();
        assertEquals(expectedUser, userService.getUserByLogin("login"));

        Mockito.when(userDao.getByLogin("login")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.getUserByLogin("login"));

        Mockito.when(daoFactory.createUserDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> userService.getUserByLogin("login"));
    }

    @Test
    void getUserByEmail() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        Mockito.when(userDao.getByEmail("email")).thenReturn(Optional.ofNullable(user));
        Mockito.when(roleDao.getRolesByUserId(1L)).thenReturn(List.of(Role.builder().setId(1L).createRole()));
        User expectedUser = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .setRoles(List.of(Role.builder().setId(1L).createRole()))
                .createUser();
        assertEquals(expectedUser, userService.getUserByEmail("email"));

        Mockito.when(userDao.getByEmail("email")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.getUserByEmail("email"));

        Mockito.when(daoFactory.createUserDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> userService.getUserByEmail("email"));
    }

    @Test
    void getUserByPhone() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        Mockito.when(userDao.getByPhone("phone")).thenReturn(Optional.ofNullable(user));
        Mockito.when(roleDao.getRolesByUserId(1L)).thenReturn(List.of(Role.builder().setId(1L).createRole()));
        User expectedUser = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .setRoles(List.of(Role.builder().setId(1L).createRole()))
                .createUser();
        assertEquals(expectedUser, userService.getUserByPhone("phone"));

        Mockito.when(userDao.getByPhone("phone")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.getUserByPhone("phone"));

        Mockito.when(daoFactory.createUserDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> userService.getUserByPhone("phone"));
    }

    @Test
    void getUserPage() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        Page<User> page = Page.<User>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Mockito.when(userDao.getPage(page)).then(invocation -> {
            Page<User> page1 = invocation.getArgument(0);
            page1.setElementsCount(1);
            page1.setData(List.of(user));
            return page1;
        });
        assertEquals("{" +
                "\"elementsCount\":1," +
                "\"limit\":5," +
                "\"content\":[" +
                "{\"id\":1,\"login\":\"login\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"," +
                "\"email\":\"email\",\"phone\":\"phone\",\"enabled\":true,\"dateCreated\":1," +
                        "\"dateUpdated\":1,\"roles\":[],\"fullName\":\"firstName lastName\"}]}",
                userService.getUserPage(page));

        Mockito.when(userDao.getPage(page)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> userService.getUserPage(page));

        Mockito.when(daoFactory.createUserDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> userService.getUserPage(page));
    }

    @Test
    void updateUserRoles() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        Role role = Role.builder()
                .setId(1L)
                .setName("name")
                .setCode("code")
                .createRole();
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .setRoles(List.of(role))
                .createUser();
        assertDoesNotThrow(() -> userService.updateUserRoles(user));

        Mockito.doThrow(DaoException.class).when(userDao).update(user);
        assertThrows(ServiceException.class, () -> userService.updateUserRoles(user));

        Mockito.doThrow(SQLException.class).when(userDao).update(user);
        assertThrows(ConnectionDBException.class, () -> userService.updateUserRoles(user));

        Mockito.doThrow(JDBCException.class).when(daoFactory).createUserDao();
        assertThrows(ConnectionDBException.class, () -> userService.updateUserRoles(user));
    }

    @Test
    void updateUserPassword() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        assertDoesNotThrow(() -> userService.updateUserPassword(user, "password"));

        Mockito.doThrow(DaoException.class).when(userDao).update(user, "password");
        assertThrows(ServiceException.class, () -> userService.updateUserPassword(user, "password"));

        Mockito.doThrow(SQLException.class).when(userDao).update(user, "password");
        assertThrows(ConnectionDBException.class, () -> userService.updateUserPassword(user, "password"));

        Mockito.doThrow(JDBCException.class).when(daoFactory).createUserDao();
        assertThrows(ConnectionDBException.class, () -> userService.updateUserPassword(user, "password"));
    }

    @Test
    void deleteUser() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        assertDoesNotThrow(() -> userService.deleteUser(user.getId()));

        Mockito.doThrow(DaoException.class).when(userDao).delete(user.getId());
        assertThrows(ServiceException.class, () -> userService.deleteUser(user.getId()));

        Mockito.doThrow(SQLException.class).when(userDao).delete(user.getId());
        assertThrows(ConnectionDBException.class, () -> userService.deleteUser(user.getId()));

        Mockito.doThrow(JDBCException.class).when(daoFactory).createUserDao();
        assertThrows(ConnectionDBException.class, () -> userService.deleteUser(user.getId()));
    }

    @Test
    void disableUserById() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        assertDoesNotThrow(() -> userService.disableUserById(user.getId()));

        Mockito.doThrow(DaoException.class).when(userDao).disable(user.getId());
        assertThrows(ServiceException.class, () -> userService.disableUserById(user.getId()));

        Mockito.doThrow(SQLException.class).when(userDao).disable(user.getId());
        assertThrows(ConnectionDBException.class, () -> userService.disableUserById(user.getId()));

        Mockito.doThrow(JDBCException.class).when(daoFactory).createUserDao();
        assertThrows(ConnectionDBException.class, () -> userService.disableUserById(user.getId()));
    }

    @Test
    void enableUserById() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        assertDoesNotThrow(() -> userService.enableUserById(user.getId()));

        Mockito.doThrow(DaoException.class).when(userDao).enable(user.getId());
        assertThrows(ServiceException.class, () -> userService.enableUserById(user.getId()));

        Mockito.doThrow(SQLException.class).when(userDao).enable(user.getId());
        assertThrows(ConnectionDBException.class, () -> userService.enableUserById(user.getId()));

        Mockito.doThrow(JDBCException.class).when(daoFactory).createUserDao();
        assertThrows(ConnectionDBException.class, () -> userService.enableUserById(user.getId()));
    }

    @Test
    void updateUserData() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        userService = new UserService(daoFactory);
        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setPhone("phone")
                .setEnabled(true)
                .setDateCreated(new Timestamp(1))
                .setDateUpdated(new Timestamp(1))
                .createUser();
        assertDoesNotThrow(() -> userService.updateUserData(user));

        Mockito.doThrow(DaoException.class).when(userDao).updateData(user);
        assertThrows(ServiceException.class, () -> userService.updateUserData(user));

        Mockito.doThrow(SQLException.class).when(userDao).updateData(user);
        assertThrows(ConnectionDBException.class, () -> userService.updateUserData(user));

        Mockito.doThrow(JDBCException.class).when(daoFactory).createUserDao();
        assertThrows(ConnectionDBException.class, () -> userService.updateUserData(user));
    }

    @Test
    void collectUserFromRequest() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("login")).thenReturn("login");
        Mockito.when(request.getParameter("firstName")).thenReturn("firstName");
        Mockito.when(request.getParameter("lastName")).thenReturn("lastName");
        Mockito.when(request.getParameter("email")).thenReturn("email");
        Mockito.when(request.getParameter("phone")).thenReturn("phone");
        userService = new UserService(daoFactory);
        User user = userService.collectUserFromRequest(request);
        Assertions.assertEquals("login", user.getLogin());
        Assertions.assertEquals("firstName", user.getFirstName());
        Assertions.assertEquals("lastName", user.getLastName());
        Assertions.assertEquals("email", user.getEmail());
        Assertions.assertEquals("phone", user.getPhone());
        Assertions.assertTrue(user.isEnabled());
    }
}