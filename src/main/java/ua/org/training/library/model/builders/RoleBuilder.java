package ua.org.training.library.model.builders;

import ua.org.training.library.model.Role;

public class RoleBuilder {
    private Long id;
    private String code;
    private String name;

    public RoleBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public RoleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public Role createRole() {
        return new Role(id, code, name);
    }
}