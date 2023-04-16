package ua.org.training.library.service;


import ua.org.training.library.dto.StatusDto;
import ua.org.training.library.model.Status;

import java.util.List;
import java.util.Optional;

public interface StatusService extends GenericService<Long, Status> {
    Optional<Status> getByCode(String code);

    List<Status> getNextStatuses(Long id);

    List<StatusDto> getNextStatusesByOrderId(long id);
}
