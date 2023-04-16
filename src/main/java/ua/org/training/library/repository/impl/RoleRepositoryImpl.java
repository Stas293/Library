package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.model.Role;
import ua.org.training.library.repository.RoleRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleDao roleDao;
    private final TransactionManager transactionManager;

    @Override
    public Optional<Role> findByCode(String code) {
        log.info("Getting role by code: {}", code);
        return roleDao.getByCode(transactionManager.getConnection(), code);
    }

    @Override
    public Optional<Role> findByName(String name) {
        log.info("Getting role by name: {}", name);
        return roleDao.getByName(transactionManager.getConnection(), name);
    }

    @Override
    public Role save(Role entity) {
        log.info("Saving role: {}", entity);
        if (entity.getId() == null) {
            log.info("Creating role: {}", entity);
            entity = roleDao.create(transactionManager.getConnection(), entity);
        } else {
            log.info("Updating role: {}", entity);
            roleDao.update(transactionManager.getConnection(), entity);
        }
        return entity;
    }

    @Override
    public List<Role> saveAll(List<Role> entities) {
        log.info("Saving roles: {}", entities);
        List<Role> rolesToSave = entities.stream()
                .filter(role -> role.getId() == null)
                .toList();
        List<Role> rolesToUpdate = entities.stream()
                .filter(role -> role.getId() != null)
                .toList();
        if (!rolesToSave.isEmpty()) {
            log.info("Creating roles: {}", rolesToSave);
            rolesToSave = roleDao.create(transactionManager.getConnection(), rolesToSave);
        }
        if (!rolesToUpdate.isEmpty()) {
            log.info("Updating roles: {}", rolesToUpdate);
            roleDao.update(transactionManager.getConnection(), rolesToUpdate);
        }
        rolesToSave.addAll(rolesToUpdate);
        return rolesToSave;
    }

    @Override
    public Optional<Role> findById(Long aLong) {
        log.info("Getting role by id: {}", aLong);
        return roleDao.getById(transactionManager.getConnection(), aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if role exists by id: {}", aLong);
        return roleDao.getById(transactionManager.getConnection(), aLong).isPresent();
    }

    @Override
    public List<Role> findAll() {
        log.info("Getting all roles");
        return roleDao.getAll(transactionManager.getConnection());
    }

    @Override
    public List<Role> findAllById(List<Long> longs) {
        log.info("Getting roles by ids: {}", longs);
        return roleDao.getByIds(transactionManager.getConnection(), longs);
    }

    @Override
    public long count() {
        log.info("Counting roles");
        return roleDao.count(transactionManager.getConnection());
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting role by id: {}", aLong);
        roleDao.delete(transactionManager.getConnection(), aLong);
    }

    @Override
    public void delete(Role entity) {
        log.info("Deleting role: {}", entity);
        roleDao.delete(transactionManager.getConnection(), entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting roles by ids: {}", ids);
        roleDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll(List<? extends Role> entities) {
        log.info("Deleting roles: {}", entities);
        List<Long> ids = entities.stream()
                .map(Role::getId)
                .toList();
        roleDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all roles");
        roleDao.deleteAll(transactionManager.getConnection());
    }

    @Override
    public List<Role> findAll(Sort sort) {
        log.info("Getting all roles sorted by: {}", sort);
        return roleDao.getAll(transactionManager.getConnection(), sort);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        log.info("Getting all roles with pagination: {}", pageable);
        return roleDao.getPage(transactionManager.getConnection(), pageable);
    }
}
