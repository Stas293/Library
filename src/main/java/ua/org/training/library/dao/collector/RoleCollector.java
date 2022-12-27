package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleCollector implements ObjectCollector<Role> {
    @Override
    public Role collectFromResultSet(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setCode(rs.getString("code"));
        role.setName(rs.getString("name"));
        return role;
    }
}
