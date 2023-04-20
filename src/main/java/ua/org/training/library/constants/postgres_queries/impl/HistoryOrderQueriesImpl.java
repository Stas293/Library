package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.HistoryOrderQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
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

    @Autowired
    public HistoryOrderQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateQuery() {
        return queries.computeIfAbsent("getCreateQuery",
                key -> queryBuilderImpl.setUp()
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
        return queryBuilderImpl.setUp()
                .select("*")
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
        if (sort == null) {
            return getSelectAllQuery();
        }
        return queries.computeIfAbsent("getSelectAllQuerySort",
                key -> selectFromHistoryOrders()
                        .orderBy(sort)
                        .build());
    }

    @Override
    public String getSelectAllQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getSelectAllQueryPage",
                    key -> selectFromHistoryOrders()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getSelectAllQuerySortPage",
                key -> selectFromHistoryOrders()
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
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
                key -> queryBuilderImpl.setUp()
                        .update("history_orders")
                        .set("book_title", "date_created",
                                "date_returned", "user_id", "status_id")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteQuery() {
        return queries.computeIfAbsent("getDeleteQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("history_orders")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("history_orders")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl.setUp()
                        .truncate("history_orders")
                        .cascade()
                        .build());
    }

    @Override
    public String getDeleteAllByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .deleteFrom("history_orders")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectAllByUserIdQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getSelectAllByUserIdQueryPage",
                    key -> selectFromHistoryOrderWhereUserId()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getSelectAllByUserIdQuerySortPage",
                key -> selectFromHistoryOrderWhereUserId()
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    private QueryBuilder selectFromHistoryOrderWhereUserId() {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("history_orders")
                .where("user_id = ?");
    }

    @Override
    public String getCountByUserIdQuery() {
        return queries.computeIfAbsent("getCountByUserIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("history_orders")
                        .where("user_id = ?")
                        .build());
    }

    @Override
    public String getSelectAllByUserIdAndSearchQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getSelectAllByUserIdAndSearchQueryPage",
                    key -> queryBuilderImpl.setUp()
                            .select("*")
                            .from("history_orders")
                            .where("user_id = ?")
                            .and("book_title LIKE ?")
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queryBuilderImpl.setUp()
                .select("*")
                .from("history_orders")
                .where("user_id = ?")
                .and("book_title LIKE ?")
                .orderBy(page.getSort())
                .limit("?")
                .offset("?")
                .build();
    }

    @Override
    public String getCountByUserIdAndSearchQuery() {
        return queries.computeIfAbsent("getCountByUserIdAndSearchQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("history_orders")
                        .where("user_id = ?")
                        .and("book_title LIKE ?")
                        .build());
    }
}
