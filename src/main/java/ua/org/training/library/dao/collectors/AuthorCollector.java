package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("authorCollector")
public class AuthorCollector implements Collector<Author> {
    @Override
    public Author collect(@NotNull ResultSet rs) throws SQLException {
        return Author.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("first_name"))
                .middleName(rs.getString("middle_name"))
                .lastName(rs.getString("last_name"))
                .build();
    }
}
