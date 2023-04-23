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
                .id(rs.getLong(1))
                .login(rs.getString(2))
                .firstName(rs.getString(3))
                .lastName(rs.getString(4))
                .phone(rs.getString(5))
                .email(rs.getString(6))
                .enabled(rs.getBoolean(9))
                .build();
    }
}
