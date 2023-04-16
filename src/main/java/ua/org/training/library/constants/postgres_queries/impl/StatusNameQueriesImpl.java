package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.StatusNameQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class StatusNameQueriesImpl implements StatusNameQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;

    @Autowired
    public StatusNameQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateStatusNameQuery() {
        return queries.computeIfAbsent("getCreateStatusNameQuery",
                key -> queryBuilderImpl.setUp()
                        .insertInto("status_name")
                        .columns("name", "lang", "status_id")
                        .values("?", "?", "?")
                        .build());
    }

    @Override
    public String getGetStatusNameByIdQuery() {
        return queries.computeIfAbsent("getGetStatusNameByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("status_name")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetStatusNameByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("status_name")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllStatusNamesQuery() {
        return queries.computeIfAbsent("getGetAllStatusNamesQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("status_name")
                        .build());
    }

    @Override
    public String getGetAllStatusNamesQuery(Sort sort) {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("status_name")
                .orderBy(sort)
                .build();
    }

    @Override
    public String getGetPageStatusNamesQuery(Pageable page) {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("status_name")
                .orderBy(page.getSort())
                .limit("?")
                .offset("?")
                .build();
    }

    @Override
    public String getUpdateStatusNameQuery() {
        return queries.computeIfAbsent("getUpdateStatusNameQuery",
                key -> queryBuilderImpl.setUp()
                        .update("status_name")
                        .set("name = ?", "lang = ?", "status_id = ?")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteStatusNameByIdQuery() {
        return queries.computeIfAbsent("getDeleteStatusNameByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("status_name")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetCountStatusNamesQuery() {
        return queries.computeIfAbsent("getGetCountStatusNamesQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("status_name")
                        .build());
    }

    @Override
    public String getDeleteAllStatusNamesQuery() {
        return queries.computeIfAbsent("getDeleteAllStatusNamesQuery",
                key -> queryBuilderImpl.setUp()
                        .truncate("status_name")
                        .build());
    }

    @Override
    public String getDeleteStatusNamesByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .deleteFrom("status_name")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getUpdateStatusNamesQuery() {
        return queries.computeIfAbsent("getUpdateStatusNamesQuery",
                key -> queryBuilderImpl.setUp()
                        .update("status_name")
                        .set("name = ?", "lang = ?", "status_id = ?")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetStatusNamesByStatusIdQuery() {
        return queries.computeIfAbsent("getGetStatusNamesByStatusIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("status_name")
                        .where("status_id = ?")
                        .build());
    }

    @Override
    public String getDeleteStatusNamesByStatusIdQuery() {
        return queries.computeIfAbsent("getDeleteStatusNamesByStatusIdQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("status_name")
                        .where("status_id = ?")
                        .build());
    }

    @Override
    public String getGetStatusNameByStatusIdAndLocaleQuery() {
        return queries.computeIfAbsent("getGetStatusNameByStatusIdAndLocaleQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("status_name")
                        .where("status_id = ?")
                        .and("lang = ?")
                        .build());
    }
}
