package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookCollector implements ObjectCollector<Book> {
    @Override
    public Book collectFromResultSet(ResultSet rs) throws SQLException {
        return Book.builder()
                .setId(rs.getLong("book_id"))
                .setName(rs.getString("book_name"))
                .setCount(rs.getInt("book_count"))
                .setISBN(rs.getString("ISBN"))
                .setPublicationDate(rs.getTimestamp("book_date_publication"))
                .setFine(rs.getDouble("fine_per_day"))
                .setLanguage(rs.getString("language"))
                .createBook();
    }
}
