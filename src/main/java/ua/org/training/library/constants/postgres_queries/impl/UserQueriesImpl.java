package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.UserQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class UserQueriesImpl implements UserQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "login");

    @Autowired
    public UserQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queries = new WeakConcurrentHashMap<>();
        this.queryBuilderImpl = queryBuilderImpl;
    }

    @Override
    public String getQueryById() {
        return queries.computeIfAbsent("getQueryById",
                key -> queryBuilderImpl
                        .select("*")
                        .from("users")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getQueryByIds(int size) {
        return queryBuilderImpl
                .select("*")
                .from("users")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getQueryAll() {
        return queries.computeIfAbsent("getQueryAll",
                key -> queryBuilderImpl
                        .select("*")
                        .from("users")
                        .build());
    }

    @Override
    public String getQueryAll(Sort sort) {
        return String.format(queries.computeIfAbsent("getQueryAllSort",
                key -> queryBuilderImpl
                        .select("*")
                        .from("users")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getQueryPage(Pageable page) {
        return String.format(queries.computeIfAbsent("getQueryPage",
                key -> queryBuilderImpl
                        .select("*, count(*) OVER() AS total")
                        .from("users")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getQueryUpdate() {
        return queries.computeIfAbsent("getQueryUpdate",
                key -> queryBuilderImpl
                        .update("users")
                        .set("login", "first_name",
                                "last_name", "email",
                                "phone", "enabled")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getQueryDelete() {
        return queries.computeIfAbsent("getQueryDelete",
                key -> queryBuilderImpl
                        .deleteFrom("users")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getQueryCount() {
        return queries.computeIfAbsent("getQueryCount",
                key -> queryBuilderImpl
                        .select("count(*)")
                        .from("users")
                        .build());
    }

    @Override
    public String getQueryDeleteAll() {
        return queries.computeIfAbsent("getQueryDeleteAll",
                key -> queryBuilderImpl
                        .truncate("users")
                        .build());
    }

    @Override
    public String getQueryDeleteByIds(int size) {
        return queryBuilderImpl
                .deleteFrom("users")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getQueryCreate() {
        return queries.computeIfAbsent("getQueryCreate",
                key -> queryBuilderImpl
                        .insertInto("users")
                        .columns("login", "first_name", "last_name", "email", "phone", "enabled", "password")
                        .values("?", "?", "?", "?", "?", "?", "?")
                        .build());
    }

    @Override
    public String getQueryUpdatePassword() {
        return queries.computeIfAbsent("getQueryUpdatePassword",
                key -> queryBuilderImpl
                        .update("users")
                        .set("password")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getQueryGetByLogin() {
        return queries.computeIfAbsent("getQueryGetByLogin",
                key -> queryBuilderImpl
                        .select("*")
                        .from("users")
                        .where("login = ?")
                        .build());
    }

    @Override
    public String getQueryGetByEmail() {
        return queries.computeIfAbsent("getQueryGetByEmail",
                key -> queryBuilderImpl
                        .select("*")
                        .from("users")
                        .where("email = ?")
                        .build());
    }

    @Override
    public String getQueryGetByPhone() {
        return queries.computeIfAbsent("getQueryGetByPhone",
                key -> queryBuilderImpl
                        .select("*")
                        .from("users")
                        .where("phone = ?")
                        .build());
    }

    @Override
    public String getQueryGetByOrderId() {
        return queries.computeIfAbsent("getQueryGetByOrderId",
                key -> queryBuilderImpl
                        .select("users.*")
                        .from("users")
                        .join("orders", "users.id = orders.user_id")
                        .where("orders.id = ?")
                        .build());
    }

    @Override
    public String getQueryGetByHistoryOrderId() {
        return queries.computeIfAbsent("getQueryGetByHistoryOrderId",
                key -> queryBuilderImpl
                        .select("users.*")
                        .from("users")
                        .join("history_orders", "users.id = history_orders.user_id")
                        .where("history_orders.id = ?")
                        .build());
    }

    @Override
    public String getQueryDisable() {
        return queries.computeIfAbsent("getQueryDisable",
                key -> queryBuilderImpl
                        .update("users")
                        .set("enabled")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getQueryEnable() {
        return queries.computeIfAbsent("getQueryEnable",
                key -> queryBuilderImpl
                        .update("users")
                        .set("enabled")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getQueryDeleteRolesByUserId() {
        return queries.computeIfAbsent("getQueryDeleteRolesByUserId",
                key -> queryBuilderImpl
                        .deleteFrom("user_role")
                        .where("user_id = ?")
                        .build());
    }

    @Override
    public String getQueryInsertRolesByUserId() {
        return queries.computeIfAbsent("getQueryInsertRolesByUserId",
                key -> queryBuilderImpl
                        .insertInto("user_role")
                        .columns("user_id", "role_id")
                        .values("?", "?")
                        .build());
    }

    @Override
    public String getQuerySearchUsers(Pageable pageable, String search) {
            return String.format(queries.computeIfAbsent("getQuerySearchUsers",
                    key -> queryBuilderImpl
                            .select("*, count(*) OVER() AS total")
                            .from("users")
                            .where("login LIKE ?")
                            .or("first_name LIKE ?")
                            .or("last_name LIKE ?")
                            .or("email LIKE ?")
                            .or("phone LIKE ?")
                            .orderBy("%s")
                            .limit("?")
                            .offset("?")
                            .build()), Utility.orderBy(pageable.getSort(), DEFAULT_SORT));
    }

}
