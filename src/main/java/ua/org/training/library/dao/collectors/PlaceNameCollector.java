package ua.org.training.library.dao.collectors;


import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.PlaceName;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("placeNameCollector")
public class PlaceNameCollector implements Collector<PlaceName> {
    @Override
    public PlaceName collect(ResultSet rs) throws SQLException {
        return PlaceName.builder()
                .id(rs.getLong(1))
                .lang(rs.getString(2))
                .name(rs.getString(3))
                .build();
    }
}
