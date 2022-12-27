package ua.org.training.library.dao.collector;

import ua.org.training.library.model.HistoryOrder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryOrderCollector implements ObjectCollector<HistoryOrder> {
    @Override
    public HistoryOrder collectFromResultSet(ResultSet rs) throws SQLException {
        return HistoryOrder.builder()
                .setId(rs.getLong("id"))
                .setDateExpire(rs.getTimestamp("date_expire"))
                .setDateCreated(rs.getTimestamp("date_created"))
                .setBookName(rs.getString("book_name"))
                .createHistoryOrder();
    }
}
