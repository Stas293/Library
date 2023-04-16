package ua.org.training.library.constants.postgres_queries.impl;

import com.project.university.system_library.constants.postgres_queries.PlaceNameQueries;
import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.utility.WeakConcurrentHashMap;
import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;
import com.project.university.system_library.utility.query.QueryBuilderImpl;

import java.util.List;
import java.util.Map;

@Component
public class PlaceNameQueriesImpl implements PlaceNameQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;

    @Autowired
    public PlaceNameQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateQuery() {
        return queries.computeIfAbsent("getCreateQuery",
                key -> queryBuilderImpl.setUp()
                        .insertInto("place_names")
                        .columns("lang", "name", "place_id")
                        .values("?", "?", "?")
                        .build());
    }

    @Override
    public String getGetByIdQuery() {
        return queries.computeIfAbsent("getGetByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("place_names")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("place_names")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllQuery() {
        return queries.computeIfAbsent("getGetAllQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("place_names")
                        .build());
    }

    @Override
    public String getGetAllQuery(Sort sort) {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("place_names")
                .orderBy(sort)
                .build();
    }

    @Override
    public String getGetPageQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getGetPageQuery",
                    key -> queryBuilderImpl.setUp()
                            .select("*")
                            .from("place_names")
                            .limit("?")
                            .offset("?")
                            .build());
        } else {
            return queryBuilderImpl.setUp()
                    .select("*")
                    .from("place_names")
                    .orderBy(page.getSort())
                    .limit("?")
                    .offset("?")
                    .build();
        }
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl.setUp()
                        .update("place_names")
                        .set("lang = ?", "name = ?", "place_id = ?")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteQuery() {
        return queries.computeIfAbsent("getDeleteQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("place_names")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("place_names")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl.setUp()
                        .truncate("place_names")
                        .build());
    }

    @Override
    public String getDeleteAllQuery(List<? extends Long> longs) {
        return queryBuilderImpl.setUp()
                .deleteFrom("place_names")
                .where("id")
                .in(longs.size())
                .build();
    }

    @Override
    public String getGetAllByPlaceIdQuery() {
        return queries.computeIfAbsent("getGetAllByPlaceIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("place_names")
                        .where("place_id = ?")
                        .build());
    }

    @Override
    public String getGetByPlaceIdAndLocaleQuery() {
        return queries.computeIfAbsent("getGetByPlaceIdAndLocaleQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("place_names")
                        .where("place_id = ?")
                        .and("lang = ?")
                        .build());
    }
}
