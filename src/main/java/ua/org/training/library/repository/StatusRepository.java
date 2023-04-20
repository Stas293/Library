package ua.org.training.library.repository;


import ua.org.training.library.model.Status;
import ua.org.training.library.repository.base.JRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface StatusRepository extends JRepository<Status, Long> {
    Optional<Status> findByCode(String code);

    List<Status> getNextStatusesForStatusById(Long id, Locale locale);

    Optional<Status> getStatusByOrderId(long id);
}
