package ua.org.training.library.dao;


import ua.org.training.library.model.Status;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface StatusDao extends GenericDao<Status> {

    Optional<Status> getByCode(Connection connection,
                               String code);

    Optional<Status> getByOrderId(Connection connection,
                                  long orderId);

    Optional<Status> getByHistoryOrderId(Connection connection,
                                         long historyId);

    List<Status> getNextStatusesForStatusById(Connection connection,
                                              Long id);
}
