package ua.org.training.library.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Controller;
import ua.org.training.library.context.annotations.mapping.Get;
import ua.org.training.library.context.annotations.mapping.Patch;
import ua.org.training.library.context.annotations.mapping.Post;
import ua.org.training.library.dto.*;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.BookService;
import ua.org.training.library.service.OrderService;
import ua.org.training.library.service.PlaceService;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.mapper.JSONMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller("/order")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {
    private final BookService bookService;
    private final RequestParamsObjectMapper requestParamsObjectMapper;
    private final JSONMapper jsonMapper;
    private final SecurityService securityService;
    private final OrderService orderService;
    private final PlaceService placeService;

    @Get("/user/page")
    public String getUser(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get user");
        return "/WEB-INF/jsp/user/page.jsp";
    }

    @Get("/user/books-to-order")
    @SneakyThrows
    public String getBooksToOrder(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get books to order");
        Pageable pageable = requestParamsObjectMapper.getPageable(request);
        String search = request.getParameter("search");
        Locale locale = Utility.getLocale(request);
        Page<BookDto> bookPage = bookService.searchBooksExceptUserOrders(
                pageable,
                securityService.getAuthorityUser(request),
                search,
                locale
        );
        jsonMapper.toJson(response.getWriter(), bookPage);
        return "";
    }

    @Get("/user/get-order")
    @SneakyThrows
    public String orderBook(HttpServletRequest request, HttpServletResponse response) {
        log.info("Order book");
        Pageable pageable = requestParamsObjectMapper.getPageable(request);
        String search = request.getParameter("search");
        String statusCode = request.getParameter("statusCode");
        Locale locale = Utility.getLocale(request);
        Page<OrderDto> orderPage = orderService.getPageByStatusAndUser(
                pageable,
                statusCode,
                securityService.getAuthorityUser(request),
                search,
                locale
        );
        jsonMapper.toJson(response.getWriter(), orderPage);
        return "";
    }

    @Get("/user/{id}")
    public String newOrder(HttpServletRequest request, HttpServletResponse response) {
        log.info("New order");
        Locale locale = Utility.getLocale(request);
        long id = Utility.getIdFromUri(request);
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        Optional<BookDto> bookDto = bookService.getBookById(id, locale, authorityUser);
        if (bookDto.isPresent()) {
            request.setAttribute("book", bookDto.get());
            List<PlaceDto> placeDtos = placeService.getAllPlaces(locale);
            request.setAttribute("places", placeDtos);
            return "/WEB-INF/jsp/user/new-order.jsp";
        }
        return "redirect:library/order/user/page";
    }

    @Post("/user/{id}")
    public String createOrder(HttpServletRequest request, HttpServletResponse response) {
        log.info("Create order");
        OrderCreationDto orderCreationDto = requestParamsObjectMapper.getOrderCreationDto(request);
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        Optional<OrderDto> order = orderService.createModel(orderCreationDto, authorityUser);
        if (order.isEmpty()) {
            return "redirect:library/order/user/page?created=false";
        }
        return "redirect:library/order/user/page?created=true";
    }

    @Get("/user/view/{id}")
    public String viewOrder(HttpServletRequest request, HttpServletResponse response) {
        log.info("View order");
        long id = Utility.getIdFromUri(request);
        Locale locale = Utility.getLocale(request);
        Optional<OrderDto> orderDto = orderService.getOrderById(id, locale);
        if (orderDto.isPresent()) {
            request.setAttribute("order", orderDto.get());
            return "/WEB-INF/jsp/user/order.jsp";
        }
        return "/WEB-INF/jsp/user/page.jsp";
    }

    @Get("/librarian/page")
    public String librarianPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Librarian page");
        return "/WEB-INF/jsp/librarian/page.jsp";
    }

    @Get("/librarian")
    @SneakyThrows
    public String librarian(HttpServletRequest request, HttpServletResponse response) {
        log.info("Librarian");
        Pageable pageable = requestParamsObjectMapper.getPageable(request);
        String search = request.getParameter("search");
        String statusCode = request.getParameter("statusCode");
        String place = request.getParameter("placeName");
        log.info("Place: {}", place);
        Page<OrderDto> orderDtoPage = orderService.getPageByStatus(
                pageable,
                statusCode,
                search,
                place
        );
        jsonMapper.toJson(response.getWriter(), orderDtoPage);
        return "";
    }

    @Get("/librarian/edit-order/{id}")
    public String editOrder(HttpServletRequest request, HttpServletResponse response) {
        log.info("Edit order");
        long id = Utility.getIdFromUri(request);
        Locale locale = Utility.getLocale(request);
        Optional<OrderDto> orderDto = orderService.getOrderForEdit(id, locale);
        if (orderDto.isPresent()) {
            request.setAttribute("order", orderDto.get());
            return "/WEB-INF/jsp/librarian/edit-order.jsp";
        }
        return "/WEB-INF/jsp/librarian/page.jsp";
    }

    @Patch("/librarian/edit-order")
    public String updateOrder(HttpServletRequest request, HttpServletResponse response) {
        log.info("Update order");
        Locale locale = Utility.getLocale(request);
        OrderUpdateDto orderUpdateDto = requestParamsObjectMapper.getOrderUpdateDto(request);
        Optional<OrderDto> orderDto = orderService.updateModel(orderUpdateDto, locale);
        if (orderDto.isEmpty()) {
            return "redirect:library/order/librarian/page?updated=false";
        }
        return "redirect:library/order/librarian/page?updated=true";
    }
}
