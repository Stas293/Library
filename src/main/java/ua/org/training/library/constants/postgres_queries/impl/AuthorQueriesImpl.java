package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.AuthorQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilder;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class AuthorQueriesImpl implements AuthorQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");

    @Autowired
    public AuthorQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }

    @Override
    public String getAuthorsByBookId() {
        return queries.computeIfAbsent("getAuthorsByBookId",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("authors a")
                        .join("book_authors ab", "a.id = ab.author_id")
                        .where("ab.book_id = ?")
                        .build());
    }

    @Override
    public String getCreateAuthor() {
        return queries.computeIfAbsent("getCreateAuthor",
                key -> queryBuilderImpl.setUp()
                        .insertInto("authors")
                        .columns("first_name", "middle_name", "last_name")
                        .values("?", "?", "?")
                        .build());
    }

    @Override
    public String getAuthorById() {
        return queries.computeIfAbsent("getBookById",
                key -> selectAuthors()
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getAllAuthors() {
        return queries.computeIfAbsent("getAllAuthors",
                key -> selectAuthors()
                        .build());
    }

    @Override
    public String getPageAuthors(Sort sort) {
        return String.format(queries.computeIfAbsent("getPageAuthors",
                key -> queryBuilderImpl.setUp()
                        .select("*, count(*) OVER() AS total")
                        .from("authors")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    private QueryBuilder selectAuthors() {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("authors");
    }

    @Override
    public String getUpdateAuthor() {
        return queries.computeIfAbsent("getUpdateAuthor",
                key -> queryBuilderImpl.setUp()
                        .update("authors")
                        .set("first_name", "middle_name", "last_name")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteAuthor() {
        return queries.computeIfAbsent("getDeleteAuthor",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("authors")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getAuthorsByIds(int size) {
        return selectAuthors()
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getCountAuthors() {
        return queries.computeIfAbsent("getCountAuthors",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("authors")
                        .build());
    }

    @Override
    public String getDeleteAllAuthors() {
        return queries.computeIfAbsent("getDeleteAllAuthors",
                key -> queryBuilderImpl.setUp()
                        .truncate("authors")
                        .cascade()
                        .build());
    }

    @Override
    public String getAllAuthors(Sort sort) {
        return String.format(queries.computeIfAbsent("getAllAuthorsSort",
                key -> queryBuilderImpl.setUp()
                        .select("*")
                        .from("authors")
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getDeleteAuthorsByIds(int size) {
        return queryBuilderImpl.setUp()
                .deleteFrom("authors")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getDeleteAuthorsByBookId() {
        return queries.computeIfAbsent("getDeleteAuthorsByBookId",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("book_authors")
                        .where("book_id = ?")
                        .build());
    }

    @Override
    public String getSaveAuthorsToBook() {
        return queries.computeIfAbsent("getSaveAuthorsToBook",
                key -> queryBuilderImpl.setUp()
                        .insertInto("book_authors")
                        .columns("book_id", "author_id")
                        .values("?", "?")
                        .build());
    }

    @Override
    public String getSearchAuthors(Sort sort) {
            return String.format(queries.computeIfAbsent("getSearchAuthors",
                    key -> queryBuilderImpl.setUp()
                            .select("*, count(*) OVER()")
                            .from("authors")
                            .where("first_name LIKE ?")
                            .or("middle_name LIKE ?")
                            .or("last_name LIKE ?")
                            .limit("?")
                            .offset("?")
                            .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getAuthorsCount() {
        return queries.computeIfAbsent("getAuthorsCount",
                key -> queryBuilderImpl.setUp()
                        .select("count(*)")
                        .from("authors")
                        .where("first_name LIKE ?")
                        .or("middle_name LIKE ?")
                        .or("last_name LIKE ?")
                        .build());
    }

    @Override
    public String getFindAllByNameContainingIgnoreCase() {
        return queries.computeIfAbsent("getFindAllByNameContainingIgnoreCase",
                key -> selectAuthors()
                        .where("first_name LIKE ?")
                        .or("middle_name LIKE ?")
                        .or("last_name LIKE ?")
                        .build());
    }
}
