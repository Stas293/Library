package ua.org.training.library.dao.collectors;


import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Keyword;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("keywordCollector")
public class KeywordCollector implements Collector<Keyword> {
    @Override
    public Keyword collect(@NotNull ResultSet rs) throws SQLException {
        return Keyword.builder()
                .id(rs.getLong("id"))
                .data(rs.getString("keyword"))
                .build();
    }
}
