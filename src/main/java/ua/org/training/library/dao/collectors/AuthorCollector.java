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
                .id(rs.getLong(1))
                .firstName(rs.getString(2))
                .middleName(rs.getString(4))
                .lastName(rs.getString(3))
                .build();
    }
}
