package ua.org.training.library.enums.constants.postgres_queries;


import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.constants.SecurityQueries;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import java.lang.ref.WeakReference;

@Component
public class SecurityQueriesImpl implements SecurityQueries {
    private final QueryBuilderImpl queryBuilderImpl;
    private WeakReference<String> passwordByLoginQuery;

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
