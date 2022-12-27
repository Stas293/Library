package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Role;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;

public class RoleService {
    private static final Logger LOGGER = LogManager.getLogger(RoleService.class);
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public RoleService() {
    }

    public RoleService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public long createRole(Role role) throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            return dao.create(role);
        } catch (SQLException e) {
            LOGGER.error("Cannot create role", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating role", e);
            throw new ServiceException(e);
        }
        return 0;
    }

    public Role getRoleById(long id) throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            return dao.getById(id).orElseThrow(() -> new ServiceException("Role not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting role by id", e);
            throw new ServiceException(e);
        }
        return null;
    }

    public String getRolePage(Page<Role> page) throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            return new PageService<Role>().jsonifyPage(dao.getPage(page));
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting role page", e);
            throw new ServiceException(e);
        }
        return null;
    }

    public void updateRole(Role role) throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            dao.update(role);
        } catch (SQLException e) {
            LOGGER.error("Cannot update role", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating role", e);
            throw new ServiceException(e);
        }
    }

    public void deleteRole(long id) throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            dao.delete(id);
        } catch (SQLException e) {
            LOGGER.error("Cannot delete role", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting role", e);
            throw new ServiceException(e);
        }
    }

    public Role getRoleByCode(String code) throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            return dao.getByCode(code).orElseThrow(() -> new ServiceException("Role not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting role by code", e);
            throw new ServiceException(e);
        }
        return null;
    }

    public Role getRoleByName(String name) throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            return dao.getByName(name).orElseThrow(() -> new ServiceException("Role not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting role by name", e);
            throw new ServiceException(e);
        }
        return null;
    }

    public List<Role> getAllRoles() throws ServiceException {
        try (RoleDao dao = daoFactory.createRoleDao()) {
            return dao.getAllRoles();
        } catch (JDBCException e) {
            LOGGER.error("Error while creating RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting all roles", e);
            throw new ServiceException(e);
        }
        return null;
    }
}
