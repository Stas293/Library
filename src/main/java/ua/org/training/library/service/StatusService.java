package ua.org.training.library.service;


import ua.org.training.library.model.Status;

import java.util.Optional;

public interface StatusService {
    Optional<Status> getByCode(String code);
}
