package ua.org.training.library.repository;


import ua.org.training.library.model.*;
import ua.org.training.library.repository.base.JRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;
import java.util.Optional;

public interface OrderRepository extends JRepository<Order, Long> {
    Page<Order> getPageByBook(Pageable page, Book book);

    Page<Order> getPageByStatusAndUser(Pageable page, Status status, User user, Locale locale);

    Page<Order> getPageByStatus(Pageable page, Status status);

    Page<Order> getPageByStatusAndUserAndSearch(Pageable page, Status statusOrder, User user, String search, Locale locale);

    Optional<Order> findById(Long aLong, Locale locale);

    Page<Order> getPageByStatusAndSearch(Pageable page, Status statusOrder, String search);

    Page<Order> getPageByStatusAndPlaceAndSearch(Pageable page, Status statusOrder, Place placeOrder, String search);

    Page<Order> getPageByStatusAndPlace(Pageable page, Status statusOrder, Place placeOrder);

    Optional<Order> findOrderByUserIdAndBookId(Long authorityUserId, long bookId);
}
