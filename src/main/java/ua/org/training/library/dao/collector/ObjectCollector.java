package ua.org.training.library.dao.collector;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectCollector<T> {
    T collectFromResultSet(ResultSet rs) throws SQLException;
}
