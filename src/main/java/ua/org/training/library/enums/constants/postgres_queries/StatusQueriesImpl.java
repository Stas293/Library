package ua.org.training.library.enums.constants.postgres_queries;


import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.constants.StatusQueries;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class StatusQueriesImpl implements StatusQueries {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "code");
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;

    @Autowired
    public StatusQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getCreateStatusQuery() {
        return queries.computeIfAbsent("getCreateStatusQuery",
                key -> queryBuilderImpl
                        .insertInto("statuses")
                        .columns("code", "closed")
                        .values("?", "?")
                        .build());
    }

    @Override
    public String getGetStatusByIdQuery() {
        return queries.computeIfAbsent("getGetStatusByIdQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("statuses")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetStatusesByIdsQuery(int size) {
        return queryBuilderImpl
                .select("*")
                .from("statuses")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllStatusesQuery() {
        return queries.computeIfAbsent("getGetAllStatusesQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("statuses")
                        .build());
    }

    @Override
    public String getGetAllStatusesQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getGetAllStatusesQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("statuses")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getGetPageStatusesQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getGetPageStatusesQuery",
                key -> queryBuilderImpl
                        .select("*, count(*) OVER() AS total")
                        .from("statuses")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getUpdateStatusQuery() {
        return queries.computeIfAbsent("getUpdateStatusQuery",
                key -> queryBuilderImpl
                        .update("statuses")
                        .set("code", "closed")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteStatusByIdQuery() {
        return queries.computeIfAbsent("getDeleteStatusByIdQuery",
                key -> queryBuilderImpl
                        .deleteFrom("statuses")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetCountStatusesQuery() {
        return queries.computeIfAbsent("getGetCountStatusesQuery",
                key -> queryBuilderImpl
                        .select("count(*)")
                        .from("statuses")
                        .build());
    }

    @Override
    public String getDeleteAllStatusesQuery() {
        return queries.computeIfAbsent("getDeleteAllStatusesQuery",
                key -> queryBuilderImpl
                        .truncate("statuses")
                        .build());
    }

    @Override
    public String getDeleteStatusesByIdsQuery(int size) {
        return queryBuilderImpl
                .deleteFrom("statuses")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetStatusByCodeQuery() {
        return queries.computeIfAbsent("getGetStatusByCodeQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("statuses")
                        .where("code = ?")
                        .build());
    }

    @Override
    public String getGetStatusByOrderIdQuery() {
        return queries.computeIfAbsent("getGetStatusByOrderIdQuery",
                key -> queryBuilderImpl
                        .select("s.*")
                        .from("statuses s")
                        .join("orders o", "s.id = o.status_id")
                        .where("o.id = ?")
                        .build());
    }

    @Override
    public String getGetStatusByHistoryOrderIdQuery() {
        return queries.computeIfAbsent("getGetStatusByHistoryOrderIdQuery",
                key -> queryBuilderImpl
                        .select("s.*")
                        .from("statuses s")
                        .join("history_orders ho", "s.id = ho.status_id")
                        .where("ho.id = ?")
                        .build());
    }

    @Override
    public String getGetNextStatusesForStatusByIdQuery() {
        return queries.computeIfAbsent("getNextStatusesForStatusById",
                key -> queryBuilderImpl
                        .select("ns2.*")
                        .from("statuses s")
                        .join("next_statuses ns", "s.id = ns.status")
                        .join("statuses ns2", "ns.next_status = ns2.id")
                        .where("s.id = ?")
                        .build());
    }
}
