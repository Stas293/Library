package ua.org.training.library.enums.constants.postgres_queries;


import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.constants.PlaceQueries;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class PlaceQueriesImpl implements PlaceQueries {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "code");
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;

    @Autowired
    public PlaceQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateQuery() {
        return queries.computeIfAbsent("getCreateQuery",
                key -> queryBuilderImpl
                        .insertInto("places")
                        .columns("code", "default_days", "choosable")
                        .values("?", "?", "?")
                        .build());
    }

    @Override
    public String getGetByIdQuery() {
        return queries.computeIfAbsent("getGetByIdQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("places")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetByIdsQuery(int size) {
        return queryBuilderImpl
                .select("*")
                .from("places")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllQuery() {
        return queries.computeIfAbsent("getGetAllQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("places")
                        .build());
    }

    @Override
    public String getGetAllQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getGetAllQuerySort",
                key -> queryBuilderImpl
                        .select("*")
                        .from("places")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getGetPageQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getGetPageQuery",
                key -> queryBuilderImpl
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
                key -> queryBuilderImpl
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
                key -> queryBuilderImpl
                        .deleteFrom("places")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl
                        .select("count(*)")
                        .from("places")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl
                        .truncate("places")
                        .build());
    }

    @Override
    public String getDeleteAllQuery(int size) {
        return queryBuilderImpl
                .deleteFrom("places")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetByOrderIdQuery() {
        return queries.computeIfAbsent("getGetByOrderIdQuery",
                key -> queryBuilderImpl
                        .select("p.*")
                        .from("places p")
                        .join("orders o", "p.id = o.place_id")
                        .where("o.id = ?")
                        .build());
    }

    @Override
    public String getGetByCodeQuery() {
        return queries.computeIfAbsent("getGetByCodeQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("places")
                        .where("code = ?")
                        .build());
    }
}
