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
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .lang(rs.getString("lang"))
                .build();
    }
}
