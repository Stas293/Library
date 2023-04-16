package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("statusCollector")
public class StatusCollector implements Collector<Status> {
    @Override
    public Status collect(@NotNull ResultSet rs) throws SQLException {
        return Status.builder()
                .id(rs.getLong("id"))
                .code(rs.getString("code"))
                .closed(rs.getBoolean("closed"))
                .build();
    }
}
