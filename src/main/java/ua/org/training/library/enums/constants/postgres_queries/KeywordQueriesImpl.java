package ua.org.training.library.enums.constants.postgres_queries;


import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.constants.KeywordQueries;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilder;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class KeywordQueriesImpl implements KeywordQueries {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "keyword");
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;


    @Autowired
    public KeywordQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getInsertQuery() {
        return queries.computeIfAbsent("getInsertQuery",
                key -> queryBuilderImpl
                        .insertInto("keywords")
                        .columns("keyword")
                        .values("?")
                        .build());
    }

    @Override
    public String getSelectByIdQuery() {
        return queries.computeIfAbsent("getSelectByIdQuery",
                key -> queryBuilderImpl
                        .select("*")
                        .from("keywords")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getSelectByIdsQuery(int size) {
        return queryBuilderImpl
                .select("*")
                .from("keywords")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectAllQuery() {
        return queries.computeIfAbsent("getSelectAllQuery",
                key -> selectFromKeywords()
                        .build());
    }

    private QueryBuilder selectFromKeywords() {
        return queryBuilderImpl
                .select("k.*, COUNT(*) OVER() AS full_count")
                .from("keywords k");
    }

    @Override
    public String getSelectAllQuery(Sort sort) {
        return String.format(queries.computeIfAbsent("getSelectAllQuerySort",
                key -> selectFromKeywords()
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getSelectAllQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSelectAllQuery",
                key -> selectFromKeywords()
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl
                        .update("keywords")
                        .set("keyword")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteByIdQuery() {
        return queries.computeIfAbsent("getDeleteByIdQuery",
                key -> queryBuilderImpl
                        .deleteFrom("keywords")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl
                        .select("count(*)")
                        .from("keywords")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl
                        .truncate("keywords")
                        .build());
    }

    @Override
    public String getDeleteByIdsQuery(int size) {
        return queryBuilderImpl
                .deleteFrom("keywords")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectByBookIdQuery() {
        return queries.computeIfAbsent("getSelectByBookIdQuery",
                key -> queryBuilderImpl
                        .select("k.*")
                        .from("keywords k")
                        .join("book_keywords bk", "bk.keyword_id = k.id")
                        .where("bk.book_id = ?")
                        .build());
    }

    @Override
    public String getDeleteByBookIdQuery() {
        return queries.computeIfAbsent("getDeleteByBookIdQuery",
                key -> queryBuilderImpl
                        .deleteFrom("book_keywords")
                        .where("book_id = ?")
                        .build());
    }

    @Override
    public String getInserKeywordToBookQuery() {
        return queries.computeIfAbsent("getInserKeywordToBookQuery",
                key -> queryBuilderImpl
                        .insertInto("book_keywords")
                        .columns("book_id", "keyword_id")
                        .values("?", "?")
                        .build());
    }

    @Override
    public String getSelectByQueryQuery() {
        return queries.computeIfAbsent("getSelectByQueryQuery",
                key -> queryBuilderImpl
                        .select("k.*")
                        .from("keywords k")
                        .where("k.keyword LIKE ?")
                        .build());
    }

    @Override
    public String getSelectByDataQuery() {
        return queries.computeIfAbsent("getSelectByDataQuery",
                key -> queryBuilderImpl
                        .select("k.*")
                        .from("keywords k")
                        .where("k.keyword = ?")
                        .build());
    }
}
