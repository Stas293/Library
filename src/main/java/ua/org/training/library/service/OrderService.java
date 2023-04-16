package ua.org.training.library.service;


import ua.org.training.library.dto.OrderCreationDto;
import ua.org.training.library.dto.OrderDto;
import ua.org.training.library.dto.OrderUpdateDto;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;
import java.util.Optional;

public interface OrderService extends GenericService<Long, Order> {
    Page<Order> getPageByBook(Pageable page, Book book);

    Page<OrderDto> getPageByStatusAndUser(Pageable page, String status, User user, String search, Locale locale);

    Page<OrderDto> getPageByStatus(Pageable page, String status, String search);

    HistoryOrder save(Order order);

    Optional<OrderDto> createModel(OrderCreationDto orderCreationDto, AuthorityUser authorityUser);

    Optional<OrderDto> getOrderById(long id, Locale locale);

    Optional<OrderDto> updateModel(OrderUpdateDto orderUpdateDto);

    Optional<OrderDto> getOrderForEdit(long id, Locale locale);
}
