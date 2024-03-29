package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("roleCollector")
public class RoleCollector implements Collector<Role> {
    @Override
    public Role collect(@NotNull ResultSet rs) throws SQLException {
        return Role.builder()
                .id(rs.getLong(1))
                .code(rs.getString(2))
                .name(rs.getString(3))
                .build();
    }
}
