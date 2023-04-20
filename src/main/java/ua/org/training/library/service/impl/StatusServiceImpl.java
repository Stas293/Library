package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.model.Status;
import ua.org.training.library.repository.StatusRepository;
import ua.org.training.library.service.StatusService;
import ua.org.training.library.utility.mapper.ObjectMapper;

import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;
    private final ObjectMapper mapper;

    @Override
    public Optional<Status> getByCode(String code) {
        log.info("Getting status by code: {}", code);
        return statusRepository.findByCode(code);
    }
}
