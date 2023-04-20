package ua.org.training.library.dao;


import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.sql.Connection;

public interface HistoryOrderDao extends GenericDao<HistoryOrder> {
    Page<HistoryOrder> getPageByUserId(Connection conn,
                                       Pageable page, Long userId);

    Page<HistoryOrder> getPageByUserIdAndSearch(Connection conn, Pageable page, Long id, String search);
}