package ua.org.training.library.dao.collector;

import ua.org.training.library.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCollector implements ObjectCollector<User> {
    @Override
    public User collectFromResultSet(ResultSet rs) throws SQLException {
        return User.builder()
                .setId(rs.getLong("id"))
                .setLogin(rs.getString("login"))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .setEmail(rs.getString("email"))
                .setPhone(rs.getString("phone"))
                .setEnabled(rs.getInt("enabled") == 1)
                .setDateCreated(rs.getDate("date_created"))
                .setDateUpdated(rs.getDate("date_updated"))
                .createUser();
    }
}
