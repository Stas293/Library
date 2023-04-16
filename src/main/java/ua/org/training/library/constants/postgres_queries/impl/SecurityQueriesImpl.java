package ua.org.training.library.constants.postgres_queries.impl;

import com.project.university.system_library.constants.postgres_queries.SecurityQueries;
import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.utility.query.QueryBuilderImpl;

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
            query = queryBuilderImpl.setUp()
                    .select("password")
                    .from("users")
                    .where("login = ?")
                    .build();
            passwordByLoginQuery = new WeakReference<>(query);
        }
        return query;
    }
}
