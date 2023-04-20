package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.dto.HistoryOrderDto;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.HistoryOrderRepository;
import ua.org.training.library.repository.UserRepository;
import ua.org.training.library.service.HistoryOrderService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryOrderServiceImpl implements HistoryOrderService {
    private final HistoryOrderRepository historyOrderRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    public Page<HistoryOrderDto> getPageByUser(Pageable page, User authority, String search, Locale locale) {
        log.info("Getting history orders by authority: {}, {}, {}", page, authority, search);
        User user = userRepository.getByLogin(authority.getLogin()).orElseThrow();
        if (search != null && !search.isEmpty()) {
            Page<HistoryOrder> historyOrderPage = historyOrderRepository.getPageByUserAndSearch(page, user, search, locale);
            return objectMapper.mapHistoryOrderPage(historyOrderPage);
        }
        Page<HistoryOrder> historyOrderPage = historyOrderRepository.getPageByUser(page, user, locale);
        return objectMapper.mapHistoryOrderPage(historyOrderPage);
    }
}
