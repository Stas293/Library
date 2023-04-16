package ua.org.training.library.dao;


import ua.org.training.library.model.Order;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.sql.Connection;

public interface OrderDao extends GenericDao<Order> {

    Page<Order> getPageByBookId(Connection conn,
                                Pageable page, Long bookId);

    Page<Order> getPageByStatusAndUserId(Connection conn, Pageable page,
                                         Long statusId, Long userId);

    Page<Order> getPageByStatusId(Connection conn,
                                  Pageable page, Long statusId);

    Page<Order> getPageByStatusAndUserAndSearch(Connection conn, Pageable page, Long statusId, Long userId, String search);

    Page<Order> getPageByStatusAndSearch(Connection conn, Pageable page, Long id, String search);
}