package ua.org.training.library.utility.query;


public interface QueryBuilder {
    QueryBuilder select(String... columns);

    QueryBuilder from(String table);

    QueryBuilder where(String clause);

    QueryBuilder join(String table, String onClause);

    QueryBuilder limit(String limit);

    QueryBuilder offset(String offset);

    QueryBuilder insertInto(String table);

    QueryBuilder values(String... values);

    QueryBuilder update(String table);

    QueryBuilder set(String... columns);

    QueryBuilder columns(String... columns);

    QueryBuilder deleteFrom(String table);

    QueryBuilder in(int size);

    QueryBuilder truncate(String table);

    String build();

    QueryBuilder except();

    QueryBuilder and(String condition);

    QueryBuilder cascade();

    QueryBuilder or(String condition);

    QueryBuilder groupBy(String condition);

    QueryBuilder setUp();

    QueryBuilder as(String alias);

    QueryBuilder orderBy(String s);
}

