package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.HistoryOrder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component("historyOrderCollector")
public class HistoryOrderCollector implements Collector<HistoryOrder> {
    @Override
    public HistoryOrder collect(@NotNull ResultSet rs) throws SQLException {
        return HistoryOrder.builder()
                .id(rs.getLong(1))
                .bookTitle(rs.getString(2))
                .dateCreated(rs.getObject(3, LocalDate.class))
                .dateReturned(rs.getObject(4, LocalDate.class))
                .build();
    }
}
