package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleCollector implements ObjectCollector<Role> {
    @Override
    public Role collectFromResultSet(ResultSet rs) throws SQLException {
        return Role.builder()
                .setId(rs.getLong("id"))
                .setCode(rs.getString("code"))
                .setName(rs.getString("name"))
                .createRole();
    }
}
