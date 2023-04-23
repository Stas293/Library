package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.PlaceNameQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.List;
import java.util.Map;

@Component
public class PlaceNameQueriesImpl implements PlaceNameQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");


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
        return String.format(queries.computeIfAbsent("getGetAllQuerySort",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("place_names")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getGetPageQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getGetPageQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*, COUNT(*) OVER()")
                        .from("place_names")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
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
