package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.SecurityDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JDBCUserDaoTest {
    private UserDao userDao;
    @BeforeEach
    void setUp() {
        try {
            userDao = DaoFactory.getInstance().createUserDao();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() throws SQLException {
        User user = User.builder()
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")
                .setRoles(List.of(new Role(1L, "USER", "User")))
                .createUser();
        long id = userDao.create(user, "Test");
        assertEquals(2, id);
    }

    @Test
    void getById() {
        User user = User.builder()
                .setId(1L)
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")

                .createUser();
        assertEquals(Optional.of(user), userDao.getById(1L));
    }

    @Test
    void getPage() {
        Page<User> page = Page.<User> builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSearch("Test")
                .setElementsCount(1)
                .setData(List.of(User.builder()
                        .setId(1L)
                        .setFirstName("Test")
                        .setLastName("Test")
                        .setEmail("Test")
                        .setLogin("Test")
                        .setPhone("Test")

                        .createUser()))
                .setSorting("ASC")
                .createPage();
        Page<User> page1 = userDao.getPage(Page.<User> builder()
                .setPageNumber(0)
                .setLimit(10)
                        .setSearch("Test")
                .setSorting("ASC")
                .createPage());
        assertEquals(page, page1);
    }

    @Test
    void update() throws SQLException, JDBCException {
        User user = User.builder()
                .setId(1L)
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")

                .setRoles(List.of(new Role(1L, "USER", "User")))
                .createUser();
        userDao.update(user);
        userDao = DaoFactory.getInstance().createUserDao();
        user.setRoles(null);
        assertEquals(Optional.of(user), userDao.getById(1L));
    }

    @Test
    void delete() throws SQLException, JDBCException {
        userDao.delete(1L);
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(Optional.empty(), userDao.getById(1L));
    }

    @Test
    void testCreate() throws SQLException {
        assertEquals(-1L, userDao.create(User.builder()
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")
                .setRoles(List.of(new Role(1L, "USER", "User")))
                .createUser()));
    }

    @Test
    void testUpdate() throws SQLException, JDBCException {
        User user = User.builder()
                .setId(1L)
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")

                .setRoles(List.of(new Role(1L, "USER", "User")))
                .createUser();
        userDao.update(user);
        userDao = DaoFactory.getInstance().createUserDao();
        user.setRoles(null);
        assertEquals(Optional.of(user), userDao.getById(1L));
    }

    @Test
    void getByLogin() {
        User user = User.builder()
                .setId(1L)
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")

                .createUser();
        assertEquals(Optional.of(user), userDao.getByLogin("Test"));
    }

    @Test
    void getByEmail() {
        User user = User.builder()
                .setId(1L)
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")

                .createUser();
        assertEquals(Optional.of(user), userDao.getByEmail("Test"));
    }

    @Test
    void getByPhone() {
        User user = User.builder()
                .setId(1L)
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("Test")
                .setLogin("Test")
                .setPhone("Test")

                .createUser();
        assertEquals(Optional.of(user), userDao.getByPhone("Test"));
    }

    @Test
    void getByOrderId() {
        assertEquals(Optional.empty(), userDao.getByOrderId(1L));
    }

    @Test
    void getByHistoryOrderId() {
        assertEquals(Optional.empty(), userDao.getByHistoryOrderId(1L));
    }

    @Test
    void disable() throws SQLException, JDBCException {
        userDao.disable(1L);
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(false, userDao.getById(1L).get().isEnabled());
    }

    @Test
    void testDisable() throws SQLException, JDBCException {
        userDao.disable("Test");
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(false, userDao.getById(1L).get().isEnabled());
    }

    @Test
    void enable() throws SQLException, JDBCException {
        userDao.enable(1L);
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(true, userDao.getById(1L).get().isEnabled());
    }

    @Test
    void testEnable() throws SQLException, JDBCException {
        userDao.enable("Test");
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(true, userDao.getById(1L).get().isEnabled());
    }

    @Test
    void updateEmail() throws SQLException, JDBCException {
        User user = DaoFactory.getInstance().createUserDao().getById(1L).get();
        userDao.updateEmail("TestEmail", DaoFactory.getInstance().createUserDao().getById(1L).get());
        user.setEmail("TestEmail");
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(Optional.of(user), userDao.getById(1L));
    }

    @Test
    void updatePhone() throws SQLException, JDBCException {
        User user = DaoFactory.getInstance().createUserDao().getById(1L).get();
        userDao.updatePhone("TestPhone", DaoFactory.getInstance().createUserDao().getById(1L).get());
        user.setPhone("TestPhone");
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(Optional.of(user), userDao.getById(1L));
    }

    @Test
    void updatePassword() throws SQLException, JDBCException {
        User user = DaoFactory.getInstance().createUserDao().getById(1L).get();
        userDao.update(DaoFactory.getInstance().createUserDao().getById(1L).get(), "TestPassword");
        userDao = DaoFactory.getInstance().createUserDao();
        SecurityDao securityDao = DaoFactory.getInstance().createSecurityDao();
        String password = securityDao.getPasswordByLogin("Test");
        assertEquals("TestPassword", password);
    }

    @Test
    void updateFirstName() throws SQLException, JDBCException {
        User user = DaoFactory.getInstance().createUserDao().getById(1L).get();
        userDao.updateFirstName("TestFirstName", DaoFactory.getInstance().createUserDao().getById(1L).get());
        user.setFirstName("TestFirstName");
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(Optional.of(user), userDao.getById(1L));
    }

    @Test
    void updateLastName() throws SQLException, JDBCException {
        User user = DaoFactory.getInstance().createUserDao().getById(1L).get();
        userDao.updateLastName("TestLastName", DaoFactory.getInstance().createUserDao().getById(1L).get());
        user.setLastName("TestLastName");
        userDao = DaoFactory.getInstance().createUserDao();
        assertEquals(Optional.of(user), userDao.getById(1L));
    }
}