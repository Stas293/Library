package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderCollector implements ObjectCollector<Order> {
    @Override
    public Order collectFromResultSet(ResultSet rs) throws SQLException {
        return Order.builder()
                .setId(rs.getLong("id"))
                .setDateCreated(rs.getTimestamp("date_created"))
                .setDateExpire(rs.getTimestamp("date_expire"))
                .createOrder();
    }
}
