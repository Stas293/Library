package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorCollector implements ObjectCollector<Author> {
    @Override
    public Author collectFromResultSet(ResultSet rs) throws SQLException {
        return Author.builder()
                .setId(rs.getLong("id"))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .createAuthor();
    }
}
