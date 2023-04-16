package ua.org.training.library.constants.postgres_queries.impl;

import com.project.university.system_library.constants.postgres_queries.OrderQueries;
import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.utility.WeakConcurrentHashMap;
import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;
import com.project.university.system_library.utility.query.QueryBuilder;
import com.project.university.system_library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class OrderQueriesImpl implements OrderQueries {
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
                .select("o.*")
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
        if (sort == null) {
            return getSelectAllQuery();
        }
        return queries.computeIfAbsent("getSelectAllQuerySort",
                key -> selectFromOrders()
                        .orderBy(sort)
                        .build());
    }

    @Override
    public String getSelectAllQuery(Pageable page) {
        if (page == null) {
            return selectFromOrders()
                    .limit("?")
                    .offset("?")
                    .build();
        }
        return queries.computeIfAbsent("getSelectAllQuery",
                key -> selectFromOrders()
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl.setUp()
                        .update("orders")
                        .set("date_created", "date_expire",
                                "book_id", "user_id", "status_id")
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
        if (page == null) {
            return queries.computeIfAbsent("getSelectAllByBookIdQuery",
                    key -> selectFromOrders()
                            .where("book_id = ?")
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getSelectAllByBookIdQuerySort",
                key -> selectFromOrders()
                        .where("book_id = ?")
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    @Override
    public String getCountByBookIdQuery() {
        return queries.computeIfAbsent("getCountByBookIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("orders")
                        .where("book_id = ?")
                        .build());
    }

    @Override
    public String getSelectAllByStatusAndUserIdQuery(Pageable page) {
        if (page == null) {
            return queries.computeIfAbsent("getSelectAllByStatusAndUserIdQuery",
                    key -> selectFromOrders()
                            .where("status_id = ?")
                            .and("user_id = ?")
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getSelectAllByStatusAndUserIdQuerySort",
                key -> selectFromOrders()
                        .where("status_id = ?")
                        .and("user_id = ?")
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    @Override
    public String getCountByStatusAndUserIdQuery() {
        return queries.computeIfAbsent("getCountByStatusAndUserIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("orders")
                        .where("status_id = ?")
                        .and("user_id = ?")
                        .build());
    }

    @Override
    public String getSelectAllByStatusIdQuery(Pageable page) {
        if (page == null) {
            return queries.computeIfAbsent("getSelectAllByStatusIdQuery",
                    key -> selectFromOrders()
                            .where("status_id = ?")
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getSelectAllByStatusIdQuerySort",
                key -> selectFromOrders()
                        .where("status_id = ?")
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    @Override
    public String getCountByStatusIdQuery() {
        return queries.computeIfAbsent("getCountByStatusIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("orders")
                        .where("status_id = ?")
                        .build());
    }

    @Override
    public String getSelectAllByStatusAndUserIdAndSearchQuery(Pageable page) {
        if (page == null) {
            return queries.computeIfAbsent("getSelectAllByStatusAndUserIdAndSearchQuery",
                    key -> selectFromOrders()
                            .join("books b", "o.book_id = b.id")
                            .where("o.status_id = ?")
                            .and("o.user_id = ?")
                            .and("b.title like ?")
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return selectFromOrders()
                .join("books b", "o.book_id = b.id")
                .where("o.status_id = ?")
                .and("o.user_id = ?")
                .and("b.title like ?")
                .orderBy(page.getSort())
                .limit("?")
                .offset("?")
                .build();
    }

    @Override
    public String getCountByStatusAndUserIdAndSearchQuery() {
        return queries.computeIfAbsent("getCountByStatusAndUserIdAndSearchQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("orders o")
                        .join("books b", "o.book_id = b.id")
                        .where("o.status_id = ?")
                        .and("o.user_id = ?")
                        .and("b.title like ?")
                        .build());
    }

    @Override
    public String getSelectAllByStatusAndSearchQuery(Pageable page) {
        if (page == null) {
            return queries.computeIfAbsent("getSelectAllByStatusAndSearchQuery",
                    key -> selectFromOrders()
                            .join("books b", "o.book_id = b.id")
                            .where("o.status_id = ?")
                            .and("b.title like ?")
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return selectFromOrders()
                .join("books b", "o.book_id = b.id")
                .where("o.status_id = ?")
                .and("b.title like ?")
                .orderBy(page.getSort())
                .limit("?")
                .offset("?")
                .build();
    }

    @Override
    public String getCountByStatusAndSearchQuery() {
        return queries.computeIfAbsent("getCountByStatusAndSearchQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("orders o")
                        .join("books b", "o.book_id = b.id")
                        .where("o.status_id = ?")
                        .and("b.title like ?")
                        .build());
    }
}
