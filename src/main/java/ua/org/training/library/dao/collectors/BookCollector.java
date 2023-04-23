package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component("bookCollector")
public class BookCollector implements Collector<Book> {
    @Override
    public Book collect(@NotNull ResultSet rs) throws SQLException {
        return Book.builder()
                .id(rs.getLong(1))
                .title(rs.getString(2))
                .description(rs.getString(3))
                .isbn(rs.getString(4))
                .publicationDate(rs.getObject(5, LocalDate.class))
                .fine(rs.getDouble(6))
                .count(rs.getLong(7))
                .language(rs.getString(8))
                .location(rs.getString(9))
                .build();
    }
}
