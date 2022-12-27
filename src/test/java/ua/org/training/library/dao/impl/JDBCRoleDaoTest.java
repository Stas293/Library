package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.model.Role;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JDBCRoleDaoTest {
    RoleDao roleDao;
    @BeforeEach
    void setUp() {
        try {
            roleDao = DaoFactory.getInstance().createRoleDao();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create_delete() throws SQLException, JDBCException {
        Role role = new Role();
        role.setName("Test");
        role.setCode("TEST");
        long id = roleDao.create(role);
        roleDao = DaoFactory.getInstance().createRoleDao();
        roleDao.delete(id);
    }

    @Test
    void getById() {
        Role role = new Role();
        role.setId(1L);
        role.setName("User");
        role.setCode("USER");
        assertEquals(Optional.of(role), roleDao.getById(1L));
    }

    @Test
    void getPage() {
        Page<Role> page = Page.<Role> builder()
                .setPageNumber(0)
                .setLimit(10)
                .setElementsCount(1)
                .setData(List.of(new Role(1L, "USER", "User")))
                .setSorting("ASC")
                .createPage();
        assertEquals(page, roleDao.getPage(Page.<Role> builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .createPage()));

    }

    @Test
    void update() throws SQLException, JDBCException {
        Role role = new Role();
        role.setId(1L);
        role.setName("User");
        role.setCode("USER");
        roleDao.update(role);
        roleDao = DaoFactory.getInstance().createRoleDao();
        assertEquals(Optional.of(role), roleDao.getById(1L));
    }

    @Test
    void getByCode() {
        Role role = new Role();
        role.setId(1L);
        role.setName("User");
        role.setCode("USER");
        assertEquals(Optional.of(role), roleDao.getByCode("USER"));
    }

    @Test
    void getByName() {
        Role role = new Role();
        role.setId(1L);
        role.setName("User");
        role.setCode("USER");
        assertEquals(Optional.of(role), roleDao.getByName("User"));
    }

    @Test
    void getRolesByUserId() {
        assertEquals(List.of(new Role(1L, "USER", "User")), roleDao.getRolesByUserId(1L));
    }

    @Test
    void getRolesByUserLogin() {
        assertEquals(List.of(new Role(1L, "USER", "User")), roleDao.getRolesByUserLogin("Test"));
    }

    @Test
    void getAllRoles() {
        assertEquals(List.of(
                new Role(1L, "USER", "User"),
                        new Role(2L, "ADMIN", "Admin"),
                        new Role(3L, "LIBRARIAN", "Librarian")), roleDao.getAllRoles());
    }
}