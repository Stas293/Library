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
        return Order.builder()
                .id(rs.getLong(1))
                .dateCreated(rs.getDate(2).toLocalDate())
                .dateExpire(rs.getObject(3, LocalDate.class))
                .build();
    }
}
