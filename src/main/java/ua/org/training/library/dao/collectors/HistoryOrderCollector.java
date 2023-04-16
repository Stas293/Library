package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.HistoryOrder;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("historyOrderCollector")
public class HistoryOrderCollector implements Collector<HistoryOrder> {
    @Override
    public HistoryOrder collect(@NotNull ResultSet rs) throws SQLException {
        return HistoryOrder.builder()
                .id(rs.getLong("id"))
                .bookTitle(rs.getString("book_title"))
                .dateCreated(rs.getDate("date_created").toLocalDate())
                .dateReturned(rs.getDate("date_returned").toLocalDate())
                .build();
    }
}
