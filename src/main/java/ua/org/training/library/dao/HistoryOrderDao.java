package ua.org.training.library.dao;

import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.utility.page.Page;

public interface HistoryOrderDao extends GenericDao<HistoryOrder> {
    Page<HistoryOrder> getPageByUserId(
            Page<HistoryOrder> page,
            Long userId);
}
