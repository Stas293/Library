package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.model.Role;
import ua.org.training.library.repository.RoleRepository;
import ua.org.training.library.service.RoleService;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role createModel(Role model) {
        log.info("Creating role: {}", model);
        return roleRepository.save(model);
    }

    @Override
    @Transactional
    public void updateModel(Role model) {
        log.info("Updating role: {}", model);
        roleRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModel(Role author) {
        log.info("Deleting role: {}", author);
        roleRepository.delete(author);
    }

    @Override
    @Transactional
    public void createModels(List<Role> models) {
        log.info("Creating roles: {}", models);
        roleRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void updateModels(List<Role> models) {
        log.info("Updating roles: {}", models);
        roleRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void deleteModels(List<Role> models) {
        log.info("Deleting roles: {}", models);
        roleRepository.deleteAll(models);
    }

    @Override
    public List<Role> getAllModels() {
        log.info("Getting all roles");
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getModelsByIds(List<Long> ids) {
        log.info("Getting roles by ids: {}", ids);
        return roleRepository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting roles");
        return roleRepository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all roles");
        roleRepository.deleteAll();
    }

    @Override
    public boolean checkIfExists(Role model) {
        log.info("Checking if role exists: {}", model);
        return roleRepository.existsById(model.getId());
    }

    @Override
    public Page<Role> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting roles by page: {}, {}", pageNumber, pageSize);
        return roleRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting role by id: {}", id);
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting roles by ids: {}", ids);
        roleRepository.deleteAllById(ids);
    }

    @Override
    public List<Role> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all roles by sort: {}, {}", sortField, sortOrder);
        return roleRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }

    @Override
    public Page<Role> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting roles by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return roleRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField)));
    }

    @Override
    public Optional<Role> getByCode(String code) {
        log.info("Getting role by code: {}", code);
        return roleRepository.findByCode(code);
    }

    @Override
    public Optional<Role> getByName(String name) {
        log.info("Getting role by name: {}", name);
        return roleRepository.findByName(name);
    }
}
