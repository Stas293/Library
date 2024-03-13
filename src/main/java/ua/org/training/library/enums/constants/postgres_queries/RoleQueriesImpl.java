package ua.org.training.library.enums.constants.postgres_queries;


import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.constants.RoleQueries;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class RoleQueriesImpl implements RoleQueries {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "name");
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;

    @Autowired
    public RoleQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateQuery() {
        return queries.computeIfAbsent("getCreateQuery",
                key -> queryBuilderImpl
                        .insertInto("roles")
                        .columns("code", "name")
                        .values("?", "?")
                        .build());
    }

    @Override
    public String getGetByIdQuery() {
        return queries.computeIfAbsent("getGetByIdQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("roles")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetByIdsQuery(int size) {
        return queryBuilderImpl
                .select("*")
                .from("roles")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllQuery() {
        return queries.computeIfAbsent("getGetAllQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("roles")
                        .build());
    }

    @Override
    public String getGetAllQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getGetAllQuerySort",
                key -> queryBuilderImpl
                        .select("*")
                        .from("roles")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getGetPageQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getGetPageQuery",
                key -> queryBuilderImpl
                        .select("*, count(*) OVER() AS total")
                        .from("roles")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl
                        .update("roles")
                        .set("code", "name")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteQuery() {
        return queries.computeIfAbsent("getDeleteQuery",
                key -> queryBuilderImpl
                        .deleteFrom("roles")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetCountQuery() {
        return queries.computeIfAbsent("getGetCountQuery",
                key -> queryBuilderImpl
                        .select("count(*)")
                        .from("roles")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl
                        .truncate("roles")
                        .build());
    }

    @Override
    public String getDeleteAllQuery(int size) {
        return queryBuilderImpl
                .deleteFrom("roles")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetByCodeQuery() {
        return queries.computeIfAbsent("getGetByCodeQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("roles")
                        .where("code = ?")
                        .build());
    }

    @Override
    public String getGetByNameQuery() {
        return queries.computeIfAbsent("getGetByCodeQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("roles")
                        .where("name = ?")
                        .build());
    }

    @Override
    public String getGetRolesByUserIdQuery() {
        return queries.computeIfAbsent("getGetRolesByUserIdQuery",
                key -> queryBuilderImpl
                        .select("r.*")
                        .from("roles r")
                        .join("user_role ur", "r.id = ur.role_id")
                        .where("ur.user_id = ?")
                        .build());
    }

    @Override
    public String getGetAllByCodesQuery(int size) {
        return queryBuilderImpl
                .select("*")
                .from("roles")
                .where("code")
                .in(size)
                .build();
    }
}
