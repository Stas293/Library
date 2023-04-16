package ua.org.training.library.dao.collectors;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface Collector<T> {
    T collect(ResultSet rs) throws SQLException;
    default List<T> collectList(@NotNull ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(collect(rs));
        }
        return list;
    }
}
