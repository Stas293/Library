package ua.org.training.library.dao;

import ua.org.training.library.model.Status;

import java.util.List;
import java.util.Optional;

public interface StatusDao extends GenericDao<Status> {

    Optional<Status> getByCode(String code);

    Optional<Status> getByOrderId(long orderId);

    Optional<Status> getByHistoryOrderId(long historyId);

    List<Status> getNextStatusesForStatusById(Long id);
}
