package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component("orderCollector")
public class OrderCollector implements Collector<Order> {
    @Override
    public Order collect(@NotNull ResultSet rs) throws SQLException {
        LocalDate dateExpire = rs.getDate("date_expire") != null ?
                rs.getDate("date_expire").toLocalDate() : null;
        return Order.builder()
                .id(rs.getLong("id"))
                .dateCreated(rs.getDate("date_created").toLocalDate())
                .dateExpire(dateExpire)
                .build();
    }
}
