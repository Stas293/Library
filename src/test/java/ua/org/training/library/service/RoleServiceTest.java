package ua.org.training.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private RoleDao roleDao;
    private RoleService roleService;

    @Test
    void createRole() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Mockito.when(roleDao.create(role)).thenReturn(1L);
        assertEquals(1L, roleService.createRole(role));

        Mockito.when(roleDao.create(role)).thenThrow(SQLException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.createRole(role));

        roleDao = Mockito.mock(RoleDao.class);
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        Mockito.when(roleDao.create(role)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> roleService.createRole(role));

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.createRole(role));
    }

    @Test
    void getRoleById() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Mockito.when(roleDao.getById(1L)).thenReturn(java.util.Optional.of(role));
        assertEquals(role, roleService.getRoleById(1L));

        Mockito.when(roleDao.getById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ServiceException.class, () -> roleService.getRoleById(1L));

        Mockito.when(roleDao.getById(1L)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> roleService.getRoleById(1L));

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.getRoleById(1L));
    }

    @Test
    void getRolePage() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Page<Role> page = Page.<Role>builder()
                .setPageNumber(1)
                .setLimit(10)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Page<Role> resultPage = Page.<Role>builder()
                .setPageNumber(1)
                .setLimit(10)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(role))
                .createPage();
        Mockito.when(roleDao.getPage(page)).thenReturn(resultPage);
        Assertions.assertEquals("{" +
                "\"elementsCount\":1," +
                "\"limit\":10," +
                "\"content\":[" +
                "{\"id\":null,\"code\":\"ADMIN\",\"name\":\"ADMIN\",\"authority\":\"ADMIN\"}]}",
                roleService.getRolePage(page));

        Mockito.when(roleDao.getPage(page)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> roleService.getRolePage(page));

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.getRolePage(page));
    }

    @Test
    void updateRole() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Assertions.assertDoesNotThrow(() -> roleService.updateRole(role));

        Mockito.doThrow(DaoException.class).when(roleDao).update(role);
        assertThrows(ServiceException.class, () -> roleService.updateRole(role));

        Mockito.doThrow(SQLException.class).when(roleDao).update(role);
        assertThrows(ConnectionDBException.class, () -> roleService.updateRole(role));

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.updateRole(role));
    }

    @Test
    void deleteRole() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setId(1L)
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Assertions.assertDoesNotThrow(() -> roleService.deleteRole(role.getId()));

        Mockito.doThrow(DaoException.class).when(roleDao).delete(role.getId());
        assertThrows(ServiceException.class, () -> roleService.deleteRole(role.getId()));

        Mockito.doThrow(SQLException.class).when(roleDao).delete(role.getId());
        assertThrows(ConnectionDBException.class, () -> roleService.deleteRole(role.getId()));

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.deleteRole(role.getId()));
    }

    @Test
    void getRoleByCode() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setId(1L)
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Mockito.when(roleDao.getByCode("ADMIN")).thenReturn(java.util.Optional.of(role));
        assertEquals(role, roleService.getRoleByCode("ADMIN"));

        Mockito.when(roleDao.getByCode("ADMIN")).thenReturn(java.util.Optional.empty());
        assertThrows(ServiceException.class, () -> roleService.getRoleByCode("ADMIN"));

        Mockito.when(roleDao.getByCode("ADMIN")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> roleService.getRoleByCode("ADMIN"));

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.getRoleByCode("ADMIN"));
    }

    @Test
    void getRoleByName() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setId(1L)
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Mockito.when(roleDao.getByName("ADMIN")).thenReturn(java.util.Optional.of(role));
        assertEquals(role, roleService.getRoleByName("ADMIN"));

        Mockito.when(roleDao.getByName("ADMIN")).thenReturn(java.util.Optional.empty());
        assertThrows(ServiceException.class, () -> roleService.getRoleByName("ADMIN"));

        Mockito.when(roleDao.getByName("ADMIN")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> roleService.getRoleByName("ADMIN"));

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.getRoleByName("ADMIN"));
    }

    @Test
    void getAllRoles() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createRoleDao()).thenReturn(roleDao);
        roleService = new RoleService(daoFactory);
        Role role = Role.builder()
                .setId(1L)
                .setName("ADMIN")
                .setCode("ADMIN")
                .createRole();
        Mockito.when(roleDao.getAllRoles()).thenReturn(List.of(role));
        assertEquals(List.of(role), roleService.getAllRoles());

        Mockito.when(roleDao.getAllRoles()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> roleService.getAllRoles());

        Mockito.when(daoFactory.createRoleDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> roleService.getAllRoles());
    }
}