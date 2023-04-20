package ua.org.training.library.service;


import ua.org.training.library.dto.HistoryOrderDto;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;

public interface HistoryOrderService {
    Page<HistoryOrderDto> getPageByUser(Pageable page, User user, String search, Locale locale);
}
