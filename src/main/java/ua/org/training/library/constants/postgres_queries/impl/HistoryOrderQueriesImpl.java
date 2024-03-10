package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.HistoryOrderQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilder;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class HistoryOrderQueriesImpl implements HistoryOrderQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "book_title");


    @Autowired
    public HistoryOrderQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateQuery() {
        return queries.computeIfAbsent("getCreateQuery",
                key -> queryBuilderImpl
                        .insertInto("history_orders")
                        .columns("book_title", "date_created", "date_returned", "user_id", "status_id")
                        .values("?", "?", "?", "?", "?")
                        .build());
    }

    @Override
    public String getSelectByIdQuery() {
        return queries.computeIfAbsent("getSelectByIdQuery",
                key -> selectFromHistoryOrders()
                        .where("id = ?")
                        .build());
    }

    private QueryBuilder selectFromHistoryOrders() {
        return queryBuilderImpl
                .select("*, COUNT(*) OVER() AS full_count")
                .from("history_orders");
    }

    @Override
    public String getSelectByIdsQuery(int size) {
        return selectFromHistoryOrders()
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectAllQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getSelectAllQuerySort",
                key -> selectFromHistoryOrders()
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getSelectAllQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllQueryPage",
                key -> selectFromHistoryOrders()
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectAllQuery() {
        return queries.computeIfAbsent("getSelectAllQuery",
                key -> selectFromHistoryOrders()
                        .build());
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl
                        .update("history_orders")
                        .set("book_title", "date_created",
                                "date_returned", "user_id", "status_id")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteQuery() {
        return queries.computeIfAbsent("getDeleteQuery",
                key -> queryBuilderImpl
                        .deleteFrom("history_orders")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl
                        .select("count(*)")
                        .from("history_orders")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl
                        .truncate("history_orders")
                        .cascade()
                        .build());
    }

    @Override
    public String getDeleteAllByIdsQuery(int size) {
        return queryBuilderImpl
                .deleteFrom("history_orders")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectAllByUserIdQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByUserIdQueryPage",
                key -> selectFromHistoryOrderWhereUserId()
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    private QueryBuilder selectFromHistoryOrderWhereUserId() {
        return queryBuilderImpl
                .select("*, COUNT(*) OVER() AS total")
                .from("history_orders")
                .where("user_id = ?");
    }

    @Override
    public String getSelectAllByUserIdAndSearchQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByUserIdAndSearchQueryPage",
                key -> queryBuilderImpl
                        .select("*, COUNT(*) OVER() AS total")
                        .from("history_orders")
                        .where("user_id = ?")
                        .and("book_title LIKE ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

}
