package ua.org.training.library.repository;


import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.base.JRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;

public interface HistoryOrderRepository extends JRepository<HistoryOrder, Long> {
    Page<HistoryOrder> getPageByUser(Pageable page, User user, Locale locale);

    Page<HistoryOrder> getPageByUserAndSearch(Pageable page, User user, String search, Locale locale);
}
