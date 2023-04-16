package ua.org.training.library.dao.collectors;


import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("userCollector")
public class UserCollector implements Collector<User> {
    @Override
    public User collect(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .enabled(rs.getBoolean("enabled"))
                .build();
    }
}
