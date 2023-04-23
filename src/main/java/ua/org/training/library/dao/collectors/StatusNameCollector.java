package ua.org.training.library.dao.collectors;


import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.StatusName;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("statusNameCollector")
public class StatusNameCollector implements Collector<StatusName> {
    @Override
    public StatusName collect(ResultSet rs) throws SQLException {
        return StatusName.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .lang(rs.getString(3))
                .build();
    }
}
