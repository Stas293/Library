package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.BookQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilder;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class BookQueriesImpl implements BookQueries {
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "title");

    @Autowired
    public BookQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }


    @Override
    public String getBooksByAuthorIdQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getBooksByAuthorIdQuery",
                    key -> getBooksWhereAuthorIdEquals()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getBooksByAuthorIdQuerySort",
                key -> getBooksWhereAuthorIdEquals()
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    private QueryBuilder getBooksWhereAuthorIdEquals() {
        return queryBuilderImpl.setUp()
                .select("b.*")
                .from("books b")
                .join("book_authors ba", "b.id = ba.book_id")
                .where("ba.author_id = ?");
    }

    @Override
    public String getBooksByLanguageQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getBooksByLanguageQuery",
                    key -> selectBooksWheleLanguageEquals()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getBooksByLanguageQuery",
                key -> selectBooksWheleLanguageEquals()
                        .orderBy(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    private QueryBuilder selectBooksWheleLanguageEquals() {
        return queryBuilderImpl.setUp()
                .select("*")
                .from("books")
                .where("language = ?");
    }

    @Override
    public String getBookByOrderIdQuery() {
        return queries.computeIfAbsent("getBookByOrderIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("b.*")
                        .from("books b")
                        .join("orders o", "b.id = o.book_id")
                        .where("o.id = ?")
                        .build());
    }

    @Override
    public String getBooksWhichUserDidNotOrderQuery(Pageable page) {
        if (page.getSort() == DEFAULT_SORT) {
            return queries.computeIfAbsent("getBooksWhichUserDidNotOrderQuery",
                    key -> selectBooksExceptOrdered()
                            .orderBy(DEFAULT_SORT)
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return selectBooksExceptOrdered()
                .orderBy(page.getSort())
                .limit("?")
                .offset("?")
                .build();
    }

    private QueryBuilder selectBooksExceptOrdered() {
        return queryBuilderImpl.setUp()
                .select("b.*")
                .from("books b")
                .except()
                .select("b.*")
                .from("books b")
                .join("orders o", "b.id = o.book_id")
                .where("o.user_id = ?");
    }

    @Override
    public String getBookByISBNQuery() {
        return queries.computeIfAbsent("getBookByISBNQuery",
                key -> selectBooks()
                        .where("isbn = ?")
                        .build());
    }

    private QueryBuilder selectBooks() {
        return queryBuilderImpl.setUp()
                .select("b.*")
                .from("books b")
                .join("book_authors ba", "b.id = ba.book_id")
                .join("authors a", "ba.author_id = a.id")
                .groupBy("b.id");
    }

    @Override
    public String getCreateBookQuery() {
        return queries.computeIfAbsent("getCreateBookQuery",
                key -> queryBuilderImpl.setUp()
                        .insertInto("books")
                        .columns("title",
                                "description",
                                "isbn",
                                "count",
                                "date_publication",
                                "fine",
                                "language",
                                "location")
                        .values("?", "?", "?", "?", "?", "?", "?", "?")
                        .build());
    }

    @Override
    public String getGetBookByIdQuery() {
        return queries.computeIfAbsent("getGetBookByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("b.*")
                        .from("books b")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetBooksByIdsQuery(int size) {
        return selectBooks()
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getGetAllBooksQuery() {
        return queries.computeIfAbsent("getGetAllBooksQuery",
                key -> selectBooks()
                        .build());
    }

    @Override
    public String getGetPageOfBooksQuery(Pageable page) {
        if (page.getSort() == null || page.getSort().isEmpty()) {
            return queries.computeIfAbsent("getGetPageOfBooksQuery",
                    key -> selectBooks()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return selectBooks()
                .orderByMinMax(page.getSort())
                .limit("?")
                .offset("?")
                .build();
    }

    @Override
    public String getUpdateBookQuery() {
        return queries.computeIfAbsent("getUpdateBookQuery",
                key -> queryBuilderImpl.setUp()
                        .update("books")
                        .set("title", "description", "isbn",
                                "count", "date_publication",
                                "fine", "language", "location")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getDeleteBookByIdQuery() {
        return queries.computeIfAbsent("getDeleteBookByIdQuery",
                key -> queryBuilderImpl.setUp()
                        .deleteFrom("books")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetCountOfBooksQuery() {
        return queries.computeIfAbsent("getGetCountOfBooksQuery",
                key -> queryBuilderImpl.setUp()
                        .select("COUNT(*)")
                        .from("books")
                        .build());
    }

    @Override
    public String getDeleteAllBooksQuery() {
        return queries.computeIfAbsent("getDeleteAllBooksQuery",
                key -> queryBuilderImpl.setUp()
                        .truncate("books")
                        .cascade()
                        .build());
    }

    @Override
    public String getDeleteBooksByIdsQuery(int size) {
        return queryBuilderImpl.setUp()
                .deleteFrom("books")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSearchBooksQuery(Pageable page) {
        if (page.getSort() == null) {
            return queries.computeIfAbsent("getSearchBooksQuery",
                    key -> selectBooksJoinRelationSearchByFieldsGroupById()
                            .limit("?")
                            .offset("?")
                            .build());
        }
        return queries.computeIfAbsent("getSearchBooksQuerySort",
                key -> selectBooksJoinRelationSearchByFieldsGroupById()
                        .orderByMinMax(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build());
    }

    private QueryBuilder selectBooksJoinRelationSearchByFieldsGroupById() {
        return queryBuilderImpl.setUp()
                .select("b.*")
                .from("books b")
                .join("book_keywords bk", "b.id = bk.book_id")
                .join("keywords k", "bk.keyword_id = k.id")
                .join("book_authors ba", "b.id = ba.book_id")
                .join("authors a", "ba.author_id = a.id")
                .where("b.title LIKE ?")
                .or("b.description LIKE ?")
                .or("a.last_name LIKE ?")
                .or("a.middle_name LIKE ?")
                .or("a.first_name LIKE ?")
                .or("k.keyword LIKE ?")
                .groupBy("b.id");
    }

    @Override
    public String getSearchBooksWhichUserDidNotOrderQuery(Pageable page) {
        return selectBooksExceptOrderedJoinRelationSearchByFieldsGroupById()
                        .orderByMinMax(page.getSort())
                        .limit("?")
                        .offset("?")
                        .build();
    }

    private QueryBuilder selectBooksExceptOrderedJoinRelationSearchByFieldsGroupById() {
        return queryBuilderImpl.setUp()
                .select("b.*")
                .from("books b")
                .join("book_keywords bk", "b.id = bk.book_id")
                .join("keywords k", "bk.keyword_id = k.id")
                .join("book_authors ba", "b.id = ba.book_id")
                .join("authors a", "ba.author_id = a.id")
                .where("b.title LIKE ?")
                .or("b.description LIKE ?")
                .or("a.last_name LIKE ?")
                .or("a.middle_name LIKE ?")
                .or("a.first_name LIKE ?")
                .or("k.keyword LIKE ?")
                .except()
                .select("b.*")
                .from("books b")
                .join("book_keywords bk", "b.id = bk.book_id")
                .join("keywords k", "bk.keyword_id = k.id")
                .join("book_authors ba", "b.id = ba.book_id")
                .join("authors a", "ba.author_id = a.id")
                .join("orders o", "b.id = o.book_id")
                .where("o.user_id = ?")
                .groupBy("b.id");
    }

    @Override
    public String getGetAllBooksQuery(Sort sort) {
        if (sort == null) {
            return queries.computeIfAbsent("getGetAllBooksQuery",
                    key -> selectBooks()
                            .build());
        }
        return selectBooks()
                .orderBy(sort)
                .build();
    }

    @Override
    public String getCountSearchBooksQuery() {
        return queries.computeIfAbsent("getCountSearchBooksQuery",
                key -> queryBuilderImpl.setUp()
                        .select("COUNT(*)")
                        .from("books b")
                        .join("book_keywords bk", "b.id = bk.book_id")
                        .join("keywords k", "bk.keyword_id = k.id")
                        .join("book_authors ba", "b.id = ba.book_id")
                        .join("authors a", "ba.author_id = a.id")
                        .where("b.title LIKE ?")
                        .or("b.description LIKE ?")
                        .or("a.last_name LIKE ?")
                        .or("a.middle_name LIKE ?")
                        .or("a.first_name LIKE ?")
                        .or("k.keyword LIKE ?")
                        .groupBy("b.id")
                        .build());
    }

    @Override
    public String getCountBooksWhichUserDidNotOrderQuery() {
        return queries.computeIfAbsent("getCountBooksWhichUserDidNotOrderQuery",
                key -> queryBuilderImpl.setUp()
                        .select("count(*) FROM (")
                        .select("b.*")
                        .from("books b")
                        .join("book_keywords bk", "b.id = bk.book_id")
                        .join("keywords k", "bk.keyword_id = k.id")
                        .join("book_authors ba", "b.id = ba.book_id")
                        .join("authors a", "ba.author_id = a.id")
                        .where("b.title LIKE ?")
                        .or("b.description LIKE ?")
                        .or("a.last_name LIKE ?")
                        .or("a.middle_name LIKE ?")
                        .or("a.first_name LIKE ?")
                        .or("k.keyword LIKE ?")
                        .except()
                        .select("b.*")
                        .from("books b")
                        .join("book_keywords bk", "b.id = bk.book_id")
                        .join("keywords k", "bk.keyword_id = k.id")
                        .join("book_authors ba", "b.id = ba.book_id")
                        .join("authors a", "ba.author_id = a.id")
                        .join("orders o", "b.id = o.book_id")
                        .where("o.user_id = ?")
                        .groupBy("b.id")
                        .as("t")
                        .build());
    }

    @Override
    public String getBookCountQuery() {
        return queries.computeIfAbsent("getBookCountQuery",
                key -> queryBuilderImpl.setUp()
                        .select("b.count")
                        .from("books b")
                        .where("b.id = ?")
                        .build());
    }

    @Override
    public String getBooksByAuthorIdQuery() {
        return queries.computeIfAbsent("getBooksByAuthorIdQuery",
                key -> queryBuilderImpl.setUp()
                        .select("b.*")
                        .from("books b")
                        .join("book_authors ba", "b.id = ba.book_id")
                        .where("ba.author_id = ?")
                        .build());
    }

    @Override
    public String getCountBooksWhichUserDidNotOrderQueryNoSearch() {
        return queries.computeIfAbsent("getCountBooksWhichUserDidNotOrderQueryNoSearch",
                key -> queryBuilderImpl.setUp()
                        .select("count(*) FROM (")
                        .select("b.*")
                        .from("books b")
                        .except()
                        .select("b.*")
                        .from("books b")
                        .join("orders o", "b.id = o.book_id")
                        .where("o.user_id = ?")
                        .groupBy("b.id")
                        .as("t")
                        .build());
    }

    @Override
    public String getExistsByIsbnQuery() {
        return queries.computeIfAbsent("getExistsByIsbnQuery",
                key -> queryBuilderImpl.setUp()
                        .select("b.id")
                        .from("books b")
                        .where("b.isbn = ?")
                        .build());
    }
}
