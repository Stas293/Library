package ua.org.training.library.model;

import ua.org.training.library.model.builders.RoleBuilder;
import ua.org.training.library.security.GrantedAuthority;

import java.util.Objects;

public class Role implements GrantedAuthority {
    private Long id;
    private String code;
    private String name;

    public Role() {
    }

    public Role(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static RoleBuilder builder() {
        return new RoleBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return code;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrantedAuthority ga = (GrantedAuthority) o;
        return (Objects.equals(getAuthority(), ga.getAuthority()));
    }

    public int hashCode() {
        return getAuthority().hashCode();
    }

    public String toString() {
        return "Role{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
