package ua.org.training.library.enums.constants.postgres_queries;


import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.constants.BookQueries;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.WeakConcurrentHashMap;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilder;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.util.Map;

@Component
public class BookQueriesImpl implements BookQueries {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "title");
    private final Map<String, String> queries;
    private final QueryBuilderImpl queryBuilderImpl;


    @Autowired
    public BookQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.queries = new WeakConcurrentHashMap<>();
    }


    @Override
    public String getBooksByAuthorIdQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getBooksByAuthorIdQuery",
                key -> queryBuilderImpl
                        .select("b.*, COUNT(*) OVER()")
                        .from("books b")
                        .join("book_authors ba", "b.id = ba.book_id")
                        .where("ba.author_id = ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getBooksByLanguageQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getBooksByLanguageQuery",
                key -> queryBuilderImpl
                        .select("*, COUNT(*) OVER()")
                        .from("books")
                        .where("language = ?")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderBy(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getBookByOrderIdQuery() {
        return queries.computeIfAbsent("getBookByOrderIdQuery",
                key -> queryBuilderImpl
                        .select("b.*")
                        .from("books b")
                        .join("orders o", "b.id = o.book_id")
                        .where("o.id = ?")
                        .build());
    }

    @Override
    public String getBooksWhichUserDidNotOrderQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getBooksWhichUserDidNotOrderQuery",
                key -> queryBuilderImpl
                        .select("b.*, COUNT(*) OVER()")
                        .from("books b")
                        .join("book_authors ba", "b.id = ba.book_id")
                        .join("authors a", "ba.author_id = a.id")
                        .where("b.id NOT IN(")
                        .select("b.id")
                        .from("books b")
                        .join("orders o", "b.id = o.book_id")
                        .where("o.user_id = ?)")
                        .groupBy("b.id")
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderByMinMax(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getBookByISBNQuery() {
        return queries.computeIfAbsent("getBookByISBNQuery",
                key -> selectBooks()
                        .where("isbn = ?")
                        .build());
    }

    private QueryBuilder selectBooks() {
        return queryBuilderImpl
                .select("b.*, COUNT(*) OVER()")
                .from("books b")
                .join("book_authors ba", "b.id = ba.book_id")
                .join("authors a", "ba.author_id = a.id")
                .groupBy("b.id");
    }

    @Override
    public String getCreateBookQuery() {
        return queries.computeIfAbsent("getCreateBookQuery",
                key -> queryBuilderImpl
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
                key -> queryBuilderImpl
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
        return String.format(
                queries.computeIfAbsent("getGetPageOfBooksQuery",
                        key -> selectBooks()
                                .orderBy("%s")
                                .limit("?")
                                .offset("?")
                                .build()), Utility.orderByMinMax(page.getSort(), DEFAULT_SORT));
    }

    @Override
    public String getUpdateBookQuery() {
        return queries.computeIfAbsent("getUpdateBookQuery",
                key -> queryBuilderImpl
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
                key -> queryBuilderImpl
                        .deleteFrom("books")
                        .where("id = ?")
                        .build());
    }

    @Override
    public String getGetCountOfBooksQuery() {
        return queries.computeIfAbsent("getGetCountOfBooksQuery",
                key -> queryBuilderImpl
                        .select("COUNT(*)")
                        .from("books")
                        .build());
    }

    @Override
    public String getDeleteAllBooksQuery() {
        return queries.computeIfAbsent("getDeleteAllBooksQuery",
                key -> queryBuilderImpl
                        .truncate("books")
                        .cascade()
                        .build());
    }

    @Override
    public String getDeleteBooksByIdsQuery(int size) {
        return queryBuilderImpl
                .deleteFrom("books")
                .where("id")
                .in(size)
                .build();
    }

    @Override
    public String getSearchBooksQuery(Pageable page) {
        return String.format(queries.computeIfAbsent("getSearchBooksQuery",
                key -> selectBooksJoinRelationSearchByFieldsGroupById()
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderByMinMax(page.getSort(), DEFAULT_SORT));
    }

    private QueryBuilder selectBooksJoinRelationSearchByFieldsGroupById() {
        return queryBuilderImpl
                .select("b.*, COUNT(*) OVER()")
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
        return String.format(queries.computeIfAbsent("getSearchBooksWhichUserDidNotOrderQuery",
                key -> selectBooksExceptOrderedJoinRelationSearchByFieldsGroupById()
                        .orderBy("%s")
                        .limit("?")
                        .offset("?")
                        .build()), Utility.orderByMinMax(page.getSort(), DEFAULT_SORT));
    }

    private QueryBuilder selectBooksExceptOrderedJoinRelationSearchByFieldsGroupById() {
        return queryBuilderImpl
                .select("b.*, COUNT(*) OVER()")
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
        return String.format(queries.computeIfAbsent("getGetAllBooksQuery",
                key -> selectBooks()
                        .orderBy("%s")
                        .build()), Utility.orderBy(sort, DEFAULT_SORT));
    }

    @Override
    public String getBookCountQuery() {
        return queries.computeIfAbsent("getBookCountQuery",
                key -> queryBuilderImpl
                        .select("b.count")
                        .from("books b")
                        .where("b.id = ?")
                        .build());
    }

    @Override
    public String getBooksByAuthorIdQuery() {
        return queries.computeIfAbsent("getBooksByAuthorIdQuery",
                key -> queryBuilderImpl
                        .select("b.*")
                        .from("books b")
                        .join("book_authors ba", "b.id = ba.book_id")
                        .where("ba.author_id = ?")
                        .build());
    }

    @Override
    public String getExistsByIsbnQuery() {
        return queries.computeIfAbsent("getExistsByIsbnQuery",
                key -> queryBuilderImpl
                        .select("b.id")
                        .from("books b")
                        .where("b.isbn = ?")
                        .build());
    }
}
