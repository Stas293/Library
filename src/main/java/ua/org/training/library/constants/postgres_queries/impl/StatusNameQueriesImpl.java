package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.StatusNameQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class StatusNameQueriesImpl implements StatusNameQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "name");

    @Autowired
    public StatusNameQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateStatusNameQuery() {
        return queries.computeIfAbsent("getCreateStatusNameQuery",
                key -> queryBuilderImpl
                        .insertInto("status_name")
                        .columns("name", "lang", "status_id")
                        .values("?", "?", "?")
                        .build());
    }

    @Override
    public String getGetStatusNameByIdQuery() {
        return queries.computeIfAbsent("getGetStatusNameByIdQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("status_name")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetStatusNameByIdsQuery(int size) {
        return queryBuilderImpl
                .select("*")
                .from("status_name")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllStatusNamesQuery() {
        return queries.computeIfAbsent("getGetAllStatusNamesQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("status_name")
                        .build());
    }

    @Override
    public String getGetAllStatusNamesQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getGetAllStatusNamesQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("status_name")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getGetPageStatusNamesQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getGetPageStatusNamesQuery",
                key -> queryBuilderImpl
                        .select("*, count(*) OVER() AS total")
                        .from("status_name")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getUpdateStatusNameQuery() {
        return queries.computeIfAbsent("getUpdateStatusNameQuery",
                key -> queryBuilderImpl
                        .update("status_name")
                        .set("name = ?", "lang = ?", "status_id = ?")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteStatusNameByIdQuery() {
        return queries.computeIfAbsent("getDeleteStatusNameByIdQuery",
                key -> queryBuilderImpl
                        .deleteFrom("status_name")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetCountStatusNamesQuery() {
        return queries.computeIfAbsent("getGetCountStatusNamesQuery",
                key -> queryBuilderImpl
                        .select("count(*)")
                        .from("status_name")
                        .build());
    }

    @Override
    public String getDeleteAllStatusNamesQuery() {
        return queries.computeIfAbsent("getDeleteAllStatusNamesQuery",
                key -> queryBuilderImpl
                        .truncate("status_name")
                        .build());
    }

    @Override
    public String getDeleteStatusNamesByIdsQuery(int size) {
        return queryBuilderImpl
                .deleteFrom("status_name")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getUpdateStatusNamesQuery() {
        return queries.computeIfAbsent("getUpdateStatusNamesQuery",
                key -> queryBuilderImpl
                        .update("status_name")
                        .set("name = ?", "lang = ?", "status_id = ?")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetStatusNamesByStatusIdQuery() {
        return queries.computeIfAbsent("getGetStatusNamesByStatusIdQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("status_name")
                        .where("status_id = ?")
                        .build());
    }

    @Override
    public String getGetStatusNameByStatusIdAndLocaleQuery() {
        return queries.computeIfAbsent("getGetStatusNameByStatusIdAndLocaleQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("status_name")
                        .where("status_id = ?")
                        .and("lang = ?")
                        .build());
    }
}
