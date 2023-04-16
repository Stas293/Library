package ua.org.training.library.utility.query;


import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.Iterator;

@Component
public class QueryBuilderImpl implements QueryBuilder {
    protected ThreadLocal<StringBuilder> query;

    public QueryBuilderImpl() {
        query = new ThreadLocal<>();
    }

    @Override
    public QueryBuilder setUp() {
        query.set(new StringBuilder());
        return this;
    }

    @Override
    public QueryBuilder as(String alias) {
        query.get().append(") AS ").append(alias);
        return this;
    }

    public QueryBuilder select(String... columns) {
        StringBuilder stringBuilder = this.query.get();
        stringBuilder.append("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return this;
    }

    public QueryBuilder from(String table) {
        this.query.get().append(" FROM ").append(table);
        return this;
    }

    public QueryBuilder where(String clause) {
        this.query.get().append(" WHERE ").append(clause);
        return this;
    }

    public QueryBuilder orderBy(Sort sort) {
        if (sort == null) {
            return this;
        }
        StringBuilder stringBuilder = this.query.get();
        Iterator<Sort.Order> sortIterator = sort.iterator();
        stringBuilder.append(" ORDER BY ");
        while (sortIterator.hasNext()) {
            Sort.Order order = sortIterator.next();
            stringBuilder.append(order.getProperty()).append(" ").append(order.getDirection());
            if (sortIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        return this;
    }

    public QueryBuilder join(String table, String onClause) {
        this.query.get().append(" JOIN ").append(table).append(" ON ").append(onClause);
        return this;
    }

    public QueryBuilder limit(String limit) {
        this.query.get().append(" LIMIT ").append(limit);
        return this;
    }

    public QueryBuilder offset(String offset) {
        this.query.get().append(" OFFSET ").append(offset);
        return this;
    }

    public QueryBuilder insertInto(String table) {
        this.query.get().append("INSERT INTO ").append(table);
        return this;
    }

    public QueryBuilder values(String... values) {
        StringBuilder stringBuilder = this.query.get();
        stringBuilder.append(" VALUES (");
        for (int i = 0; i < values.length; i++) {
            stringBuilder.append(values[i]);
            if (i < values.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        return this;
    }

    public QueryBuilder update(String table) {
        this.query.get().append("UPDATE ").append(table);
        return this;
    }

    public QueryBuilder set(String... columns) {
        StringBuilder stringBuilder = this.query.get();
        stringBuilder.append(" SET ");
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return this;
    }

    public QueryBuilder columns(String... columns) {
        StringBuilder stringBuilder = this.query.get();
        stringBuilder.append(" (");
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        return this;
    }

    public QueryBuilder deleteFrom(String table) {
        this.query.get().append("DELETE FROM ").append(table);
        return this;
    }

    public QueryBuilder in(int size) {
        StringBuilder stringBuilder = this.query.get();
        stringBuilder.append(" IN (");
        for (int i = 0; i < size; i++) {
            stringBuilder.append("?");
            if (i < size - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        return this;
    }

    public QueryBuilder truncate(String table) {
        this.query.get().append("TRUNCATE ").append(table);
        return this;
    }

    @Override
    public QueryBuilder except() {
        this.query.get().append(" EXCEPT ");
        return this;
    }

    @Override
    public QueryBuilder and(String condition) {
        this.query.get().append(" AND ").append(condition);
        return this;
    }

    @Override
    public QueryBuilder cascade() {
        this.query.get().append(" CASCADE");
        return this;
    }

    @Override
    public QueryBuilder or(String condition) {
        this.query.get().append(" OR ").append(condition);
        return this;
    }

    @Override
    public QueryBuilder groupBy(String condition) {
        this.query.get().append(" GROUP BY ").append(condition);
        return this;
    }

    public String build() {
        String result = query.get().toString();
        query.remove();
        return result;
    }
}
