package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusCollector implements ObjectCollector<Status>{
    @Override
    public Status collectFromResultSet(ResultSet rs) throws SQLException {
        Status status = new Status();
        status.setId(rs.getLong("id"));
        status.setCode(rs.getString("code"));
        status.setName(rs.getString("name"));
        status.setClosed(rs.getBoolean("closed"));
        return status;
    }
}
