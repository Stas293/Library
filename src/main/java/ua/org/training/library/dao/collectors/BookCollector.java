package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("bookCollector")
public class BookCollector implements Collector<Book> {
    @Override
    public Book collect(@NotNull ResultSet rs) throws SQLException {
        return Book.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .isbn(rs.getString("isbn"))
                .count(rs.getLong("count"))
                .publicationDate(rs.getDate("date_publication").toLocalDate())
                .fine(rs.getDouble("fine"))
                .language(rs.getString("language"))
                .location(rs.getString("location"))
                .build();
    }
}
