package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

public interface UserQueries {
    String getQueryById();

    String getQueryByIds(int size);

    String getQueryAll();

    String getQueryAll(Sort sort);

    String getQueryPage(Pageable page);

    String getQueryUpdate();

    String getQueryDelete();

    String getQueryCount();

    String getQueryDeleteAll();

    String getQueryDeleteByIds(int size);

    String getQueryCreate();

    String getQueryUpdatePassword();

    String getQueryGetByLogin();

    String getQueryGetByEmail();

    String getQueryGetByPhone();

    String getQueryGetByOrderId();

    String getQueryGetByHistoryOrderId();

    String getQueryDisable();

    String getQueryEnable();

    String getQueryDeleteRolesByUserId();

    String getQueryInsertRolesByUserId();
}
