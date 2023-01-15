package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Status;
import ua.org.training.library.model.builders.StatusBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusCollector implements ObjectCollector<Status>{
    @Override
    public Status collectFromResultSet(ResultSet rs) throws SQLException {
        return Status.builder()
                .setId(rs.getLong("id"))
                .setCode(rs.getString("code"))
                .setName(rs.getString("name"))
                .setClosed(rs.getBoolean("closed"))
                .createStatus();
    }
}
