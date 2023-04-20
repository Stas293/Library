package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.dto.RoleDto;
import ua.org.training.library.model.Role;
import ua.org.training.library.repository.RoleRepository;
import ua.org.training.library.service.RoleService;
import ua.org.training.library.utility.mapper.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

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

    @Override
    public List<RoleDto> getAllRoles() {
        log.info("Getting all roles");
        List<Role> roles = roleRepository.findAll();
        return roles.parallelStream()
                .map(objectMapper::mapRoleToRoleDto)
                .toList();
    }

    @Override
    public String[] findAllCode() {
        log.info("Getting all roles code");
        List<Role> roles = roleRepository.findAll();
        return roles.parallelStream()
                .map(Role::getCode)
                .toArray(String[]::new);
    }
}
