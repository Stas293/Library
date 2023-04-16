package ua.org.training.library.constants.postgres_queries.impl;

import com.project.university.system_library.constants.postgres_queries.PlaceQueries;
import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.utility.WeakConcurrentHashMap;
import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;
import com.project.university.system_library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class PlaceQueriesImpl implements PlaceQueries {
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
        return queryBuilderImpl.setUp()
                .select("*")
                .from("places")
                .orderBy(sort)
                .build();
    }

    @Override
    public String getGetPageQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getGetPageQuery",
                    key -> queryBuilderImpl.setUp()
                            .select("*")
                            .from("places")
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queryBuilderImpl.setUp()
                .select("*")
                .from("places")
                .orderBy(page.getSort())
                .limit("?")
                .offset("?")
                .build();
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