package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.OrderQueries;
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
public class OrderQueriesImpl implements OrderQueries {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "date_created");
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;

    @Autowired
    public OrderQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateQuery() {
        return queries.computeIfAbsent("getCreateQuery",
                key -> queryBuilderImpl.setUp()
                        .insertInto("orders")
                        .columns("date_created", "book_id", "status_id", "user_id", "place_id")
                        .values("?", "?", "?", "?", "?")
                        .build());
    }

    @Override
    public String getSelectByIdQuery() {
        return queries.computeIfAbsent("getSelectByIdQuery",
                key -> selectFromOrders()
                        .where("id = ?")
                        .build());
    }

    private QueryBuilder selectFromOrders() {
        return queryBuilderImpl.setUp()
                .select("o.*, COUNT(*) OVER()")
                .from("orders o");
    }

    @Override
    public String getSelectByIdsQuery(int size) {
        return selectFromOrders()
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectAllQuery() {
        return queries.computeIfAbsent("getSelectAllQuery",
                key -> selectFromOrders()
                        .build());
    }

    @Override
    public String getSelectAllQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getSelectAllQuerySort",
                key -> selectFromOrders()
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getSelectAllQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllQueryPage",
                key -> selectFromOrders()
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderByMinMax(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl.setUp()
                        .update("orders")
                        .set("date_created", "date_expire", "status_id",
                                "user_id", "book_id")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteQuery() {
        return queries.computeIfAbsent("getDeleteQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("orders")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("orders")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl.setUp()
                        .truncate("orders")
                        .build());
    }

    @Override
    public String getDeleteByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .deleteFrom("orders")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectAllByBookIdQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByBookIdQuery",
                key -> selectFromOrders()
                        .where("book_id = ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectAllByStatusAndUserIdQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByStatusAndUserIdQuery",
                key -> selectFromOrders()
                        .join("books b", "b.id = o.book_id")
                        .join("users u", "u.id = o.user_id")
                        .join("places p", "p.id = o.place_id")
                        .where("status_id = ?")
                        .and("user_id = ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectAllByStatusIdQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByStatusIdQuery",
                key -> selectFromOrders()
                        .join("books b", "b.id = o.book_id")
                        .join("users u", "u.id = o.user_id")
                        .where("status_id = ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectAllByStatusAndUserIdAndSearchQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByStatusAndUserIdAndSearchQuery",
                key -> selectFromOrders()
                        .join("books b", "o.book_id = b.id")
                        .where("o.status_id = ?")
                        .and("o.user_id = ?")
                        .and("b.title like ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectAllByStatusAndSearchQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByStatusAndSearchQuery",
                key -> selectFromOrders()
                        .join("books b", "o.book_id = b.id")
                        .where("o.status_id = ?")
                        .and("b.title like ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectAllByStatusAndPlaceAndSearchQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByStatusAndPlaceAndSearchQuery",
                key -> selectFromOrders()
                        .join("books b", "o.book_id = b.id")
                        .where("o.status_id = ?")
                        .and("o.place_id = ?")
                        .and("b.title like ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectAllByStatusAndPlaceQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllByStatusAndPlaceQuery",
                key -> selectFromOrders()
                        .join("books b", "o.book_id = b.id")
                        .join("users u", "o.user_id = u.id")
                        .where("status_id = ?")
                        .and("place_id = ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getSelectByUserIdAndBookIdQuery() {
        return queries.computeIfAbsent("getSelectByUserIdAndBookIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("orders")
                        .where("user_id = ?")
                        .and("book_id = ?")
                        .build());
    }

    @Override
    public String getSelectByBookIdQuery() {
        return queries.computeIfAbsent("getSelectByBookIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("orders")
                        .where("book_id = ?")
                        .build());
    }
}
