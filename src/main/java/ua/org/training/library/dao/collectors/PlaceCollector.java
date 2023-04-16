package ua.org.training.library.dao.collectors;


import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Place;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("placeCollector")
public class PlaceCollector implements Collector<Place> {
    @Override
    public Place collect(ResultSet rs) throws SQLException {
        return Place.builder()
                .id(rs.getLong("id"))
                .code(rs.getString("code"))
                .defaultDays(rs.getInt("default_days"))
                .choosable(rs.getBoolean("choosable"))
                .build();
    }
}
