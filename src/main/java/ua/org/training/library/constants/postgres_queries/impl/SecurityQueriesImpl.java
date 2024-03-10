package ua.org.training.library.constants.postgres_queries.impl;


import ua.org.training.library.constants.postgres_queries.SecurityQueries;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.lang.ref.WeakReference;

@Component
public class SecurityQueriesImpl implements SecurityQueries {
    private WeakReference<String> passwordByLoginQuery;
    private final QueryBuilderImpl queryBuilderImpl;

    @Autowired
    public SecurityQueriesImpl(QueryBuilderImpl queryBuilderImpl) {
        this.queryBuilderImpl = queryBuilderImpl;
        this.passwordByLoginQuery = new WeakReference<>(null);
    }

    @Override
    public String getGetPasswordByLoginQuery() {
        String query = passwordByLoginQuery.get();
        if (query == null) {
            query = queryBuilderImpl
                    .select("password")
                    .from("users")
                    .where("login = ?")
                    .build();
            passwordByLoginQuery = new WeakReference<>(query);
        }
        return query;
    }
}
