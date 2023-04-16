package ua.org.training.library.service;


import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

public interface HistoryOrderService extends GenericService<Long, HistoryOrder> {
    Page<HistoryOrder> getPageByUser(Pageable page, long userId);
}
