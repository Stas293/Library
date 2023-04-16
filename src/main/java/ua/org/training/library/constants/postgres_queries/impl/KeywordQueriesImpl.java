package ua.org.training.library.constants.postgres_queries.impl;

import com.project.university.system_library.constants.postgres_queries.KeywordQueries;
import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.utility.WeakConcurrentHashMap;
import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;
import com.project.university.system_library.utility.query.QueryBuilder;
import com.project.university.system_library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class KeywordQueriesImpl implements KeywordQueries {
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
                key -> queryBuilderImpl.setUp()
                        .insertInto("keywords")
                        .columns("keyword")
                        .values("?")
                        .build());
    }

    @Override
    public String getSelectByIdQuery() {
        return queries.computeIfAbsent("getSelectByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("keywords")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getSelectByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
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
        return queryBuilderImpl.setUp()
                .select("*")
                .from("keywords");
    }

    @Override
    public String getSelectAllQuery(Sort sort) {
        if (sort == null) {
            return getSelectAllQuery();
        }
        return queries.computeIfAbsent("getSelectAllQuery",
                key -> selectFromKeywords()
                        .orderBy(sort)
                        .build());
    }

    @Override
    public String getSelectAllQuery(Pageable page) {
        if (page == null) {
            return queries.computeIfAbsent("getSelectAllQuery",
                    key -> selectFromKeywords()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getSelectAllQuerySort",
                key -> selectFromKeywords()
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    @Override
    public String getUpdateQuery() {
        return queries.computeIfAbsent("getUpdateQuery",
                key -> queryBuilderImpl.setUp()
                        .update("keywords")
                        .set("keyword")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteByIdQuery() {
        return queries.computeIfAbsent("getDeleteByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("keywords")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getCountQuery() {
        return queries.computeIfAbsent("getCountQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("keywords")
                        .build());
    }

    @Override
    public String getDeleteAllQuery() {
        return queries.computeIfAbsent("getDeleteAllQuery",
                key -> queryBuilderImpl.setUp()
                        .truncate("keywords")
                        .build());
    }

    @Override
    public String getDeleteByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .deleteFrom("keywords")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSelectByBookIdQuery() {
        return queries.computeIfAbsent("getSelectByBookIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("k.*")
                        .from("keywords k")
                        .join("book_keywords bk", "bk.keyword_id = k.id")
                        .where("bk.book_id = ?")
                        .build());
    }

    @Override
    public String getDeleteByBookIdQuery() {
        return queries.computeIfAbsent("getDeleteByBookIdQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("book_keywords")
                        .where("book_id = ?")
                        .build());
    }

    @Override
    public String getInserKeywordToBookQuery() {
        return queries.computeIfAbsent("getInserKeywordToBookQuery",
                key -> queryBuilderImpl.setUp()
                        .insertInto("book_keywords")
                        .columns("book_id", "keyword_id")
                        .values("?", "?")
                        .build());
    }
}
