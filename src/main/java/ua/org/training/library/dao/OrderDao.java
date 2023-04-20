package ua.org.training.library.dao;


import ua.org.training.library.model.Order;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.sql.Connection;
import java.util.Optional;

public interface OrderDao extends GenericDao<Order> {

    Page<Order> getPageByBookId(Connection conn,
                                Pageable page, Long bookId);

    Page<Order> getPageByStatusAndUserId(Connection conn, Pageable page,
                                         Long statusId, Long userId);

    Page<Order> getPageByStatusId(Connection conn,
                                  Pageable page, Long statusId);

    Page<Order> getPageByStatusAndUserAndSearch(Connection conn, Pageable page, Long statusId, Long userId, String search);

    Page<Order> getPageByStatusAndSearch(Connection conn, Pageable page, Long id, String search);

    Page<Order> getPageByStatusAndPlaceAndSearch(Connection conn, Pageable page, Long statusOrderId, Long placeOrderId, String search);

    Page<Order> getPageByStatusAndPlace(Connection conn, Pageable page, Long statusOrderId, Long placeOrderId);

    Optional<Order> getOrderByUserIdAndBookId(Connection conn, Long authorityUserId, long bookId);
}