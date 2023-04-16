package ua.org.training.library.dao;


import ua.org.training.library.model.StatusName;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface StatusNameDao extends GenericDao<StatusName> {
    List<StatusName> getByStatusId(Connection conn, Long id);

    Optional<StatusName> getByStatusId(Connection conn, Long statusId, Locale locale);
}
