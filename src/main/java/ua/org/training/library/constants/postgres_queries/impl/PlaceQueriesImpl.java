package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.PlaceQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class PlaceQueriesImpl implements PlaceQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "code");

    @Autowired
    public PlaceQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateQuery() {
        return queries.computeIfAbsent("getCreateQuery",
                key -> queryBuilderImpl.setUp()
                        .insertInto("places")
                        .columns("code", "default_days", "choosable")
                        .values("?", "?", "?")
                        .build());
    }

    @Override
    public String getGetByIdQuery() {
        return queries.computeIfAbsent("getGetByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("places")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("places")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllQuery() {
        return queries.computeIfAbsent("getGetAllQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("places")
                        .build());
    }

    @Override
    public String getGetAllQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getGetAllQuerySort",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("places")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getGetPageQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getGetPageQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("places")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl.setUp()
                        .update("places")
                        .set("code = ?")
                        .set("default_days = ?")
                        .set("choosable = ?")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteQuery() {
        return queries.computeIfAbsent("getDeleteQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("places")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("places")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl.setUp()
                        .truncate("places")
                        .build());
    }

    @Override
    public String getDeleteAllQuery(int size) {
        return queryBuilderImpl.setUp()
                .deleteFrom("places")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetByOrderIdQuery() {
        return queries.computeIfAbsent("getGetByOrderIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("p.*")
                        .from("places p")
                        .join("orders o", "p.id = o.place_id")
                        .where("o.id = ?")
                        .build());
    }

    @Override
    public String getGetByCodeQuery() {
        return queries.computeIfAbsent("getGetByCodeQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("places")
                        .where("code = ?")
                        .build());
    }
}
