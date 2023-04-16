package ua.org.training.library.constants.postgres_queries.impl;

import com.project.university.system_library.constants.postgres_queries.AuthorQueries;
import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.utility.WeakConcurrentHashMap;
import com.project.university.system_library.utility.page.impl.Sort;
import com.project.university.system_library.utility.query.QueryBuilder;
import com.project.university.system_library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class AuthorQueriesImpl implements AuthorQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;

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
        if (sort == null) {
            return queries.computeIfAbsent("getPageAuthors",
                    key -> selectAuthors()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getPageAuthorsSort",
                key -> selectAuthors()
                        .orderBy(sort)
                        .limit("?")
                        .offset("?")
                        .build());
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
        if (sort == null) {
            return getAllAuthors();
        }
        return queries.computeIfAbsent("getAllAuthorsSort",
                key -> selectAuthors()
                        .orderBy(sort)
                        .build());
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
}