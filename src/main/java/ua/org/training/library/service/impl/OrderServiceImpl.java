package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.OrderCreationDto;
import ua.org.training.library.dto.OrderDto;
import ua.org.training.library.dto.OrderUpdateDto;
import ua.org.training.library.model.*;
import ua.org.training.library.repository.*;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.service.OrderService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final HistoryOrderRepository historyOrderRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PlaceRepository placeRepository;

    @Override
    public Page<Order> getPageByBook(Pageable page, Book book) {
        log.info("Getting orders by book: {}, {}", page, book);
        return orderRepository.getPageByBook(page, book);
    }

    @Override
    public Page<OrderDto> getPageByStatusAndUser(Pageable page, String status, User user, String search, Locale locale) {
        log.info("Getting orders by status and user: {}, {}, {}", page, status, user);
        Status statusOrder = statusRepository.findByCode(status).orElseThrow();
        User userOrder = userRepository.getByLogin(user.getLogin()).orElseThrow();
        if (search != null && !search.isEmpty()) {
            Page<Order> orderPage = orderRepository.getPageByStatusAndUserAndSearch(page, statusOrder, userOrder, search, locale);
            return objectMapper.mapOrderPageToOrderDtoPage(orderPage);
        }
        Page<Order> pageByStatusAndUser = orderRepository.getPageByStatusAndUser(page, statusOrder, userOrder, locale);
        return objectMapper.mapOrderPageToOrderDtoPage(pageByStatusAndUser);
    }

    @Override
    public Page<OrderDto> getPageByStatus(Pageable page, String status, String search, String place) {
        log.info("Getting orders by status: {}, {}", page, status);
        if (place != null) {
            return getPageByStatusAndPlace(page, status, search, place);
        }
        Status statusOrder = statusRepository.findByCode(status).orElseThrow();
        if (search != null && !search.isEmpty()) {
            Page<Order> orderPage = orderRepository.getPageByStatusAndSearch(page, statusOrder, search);
            return objectMapper.mapOrderLibrarianToOrderDto(orderPage);
        }
        Page<Order> pageByStatus = orderRepository.getPageByStatus(page, statusOrder);
        return objectMapper.mapOrderLibrarianToOrderDto(pageByStatus);
    }

    private Page<OrderDto> getPageByStatusAndPlace(Pageable page, String status, String search, String place) {
        Status statusOrder = statusRepository.findByCode(status).orElseThrow();
        Place placeOrder = placeRepository.getByCode(place).orElseThrow();
        if (search != null && !search.isEmpty()) {
            Page<Order> orderPage = orderRepository.getPageByStatusAndPlaceAndSearch(page, statusOrder, placeOrder, search);
            return objectMapper.mapOrderLibrarianToOrderDto(orderPage);
        }
        Page<Order> pageByStatusAndPlace = orderRepository.getPageByStatusAndPlace(page, statusOrder, placeOrder);
        return objectMapper.mapOrderLibrarianToOrderDto(pageByStatusAndPlace);
    }

    @Override
    @Transactional
    public HistoryOrder save(Order order) {
        log.info("Saving order: {}", order);
        return historyOrderRepository.save(
                objectMapper.mapOrderToHistoryOrder(
                        orderRepository.findById(order.getId()).orElseThrow()
                )
        );
    }

    @Override
    @Transactional
    public Optional<OrderDto> createModel(OrderCreationDto orderCreationDto, AuthorityUser authorityUser) {
        log.info("Creating order: {}, {}", orderCreationDto, authorityUser);
        Book book = bookRepository.findById(orderCreationDto.getBookId()).orElseThrow();
        if (book.getCount() == 0) {
            return Optional.empty();
        }
        Place place = placeRepository.findById(orderCreationDto.getPlaceId()).orElseThrow();
        User user = userRepository.getByLogin(authorityUser.getLogin()).orElseThrow();
        Status status = statusRepository.findByCode(Values.REGISTERED).orElseThrow();
        Order order = Order.builder()
                .user(user)
                .book(book)
                .place(place)
                .status(status)
                .dateCreated(LocalDate.now())
                .build();
        book.setCount(book.getCount() - 1);
        long count = bookRepository.getBookCount(book.getId());
        if (count == 0) {
            return Optional.empty();
        }
        bookRepository.updateBookCount(book);
        orderRepository.save(order);
        log.info("Saved order: {}", order);
        return Optional.of(objectMapper.mapOrderSimpleToOrderDto(order));
    }

    @Override
    public Optional<OrderDto> getOrderById(long id, Locale locale) {
        log.info("Getting order by id: {}", id);
        return orderRepository.findById(id, locale).map(objectMapper::mapOrderToOrderDto);
    }

    @Override
    @Transactional
    public Optional<OrderDto>  updateModel(OrderUpdateDto orderUpdateDto, Locale locale) {
        log.info("Updating order: {}", orderUpdateDto);
        Order order = orderRepository.findById(orderUpdateDto.getId(), locale).orElseThrow();
        log.info("Order: {}", order);
        Status previousStatus = order.getStatus();
        log.info("Previous status: {}", previousStatus);
        Status status = statusRepository.findByCode(orderUpdateDto.getStatus()).orElseThrow();
        log.info("New status: {}", status);
        order.setStatus(status);
        if (Boolean.TRUE.equals(status.getClosed())) {
            log.info("Order is closed");
            LocalDate dateExpiration = orderUpdateDto.getDateExpire();
            order.setDateExpire(dateExpiration);
            log.info("Date expiration: {}", dateExpiration);
            orderRepository.delete(order);
            log.info("Order deleted");
            Book book = order.getBook();
            book.setCount(book.getCount() + 1);
            bookRepository.updateBookCount(book);
            log.info("Book count updated");
            HistoryOrder historyOrder = objectMapper.mapOrderToHistoryOrder(order);
            log.info("History order: {}", historyOrder);
            if (previousStatus.getCode().equals(Values.REGISTERED)) {
                historyOrder.setDateReturned(null);
            }
            historyOrderRepository.save(historyOrder);
        } else {
            log.info("Order is not closed");
            order.setDateExpire(orderUpdateDto.getDateExpire());
            orderRepository.save(order);
        }
        return Optional.of(objectMapper.mapOrderSimpleToOrderDto(order));
    }

    @Override
    public Optional<OrderDto> getOrderForEdit(long id, Locale locale) {
        log.info("Getting order for edit by id: {}", id);
        Optional<Order> order = orderRepository.findById(id, locale);
        if (order.isPresent()) {
            Status status = order.get().getStatus();
            List<Status> nextStatuses = statusRepository.getNextStatusesForStatusById(status.getId(), locale);
            status.setNextStatuses(nextStatuses);
            return Optional.of(objectMapper.mapOrderToOrderDto(order.get()));
        }
        return Optional.empty();
    }
}
