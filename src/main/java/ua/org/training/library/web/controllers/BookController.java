package ua.org.training.library.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Controller;
import ua.org.training.library.context.annotations.Get;
import ua.org.training.library.dto.BookDto;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.mapper.JSONMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.mapper.ObjectMapper;

import java.util.Optional;

@Controller("/books")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookController {
    private final BookService bookService;
    private final RequestParamsObjectMapper requestParamsObjectMapper;
    private final JSONMapper jsonMapper;
    private final ObjectMapper objectMapper;

    @Get
    public String getBooks(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get books");
        return "/WEB-INF/books.jsp";
    }

    @Get("/page")
    @SneakyThrows
    public String getBookPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get books page");
        Pageable pageable = requestParamsObjectMapper.getBookPageable(request);
        String search = request.getParameter("search");
        Page<BookDto> bookDtoPage = bookService.searchBooks(pageable, search);
        jsonMapper.toJson(response.getWriter(), bookDtoPage);
        return "";
    }

    @Get("/{id}")
    public String getBookById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get book by id");
        long id = Utility.getIdFromUri(request);
        Optional<BookDto> bookDto = bookService.getBookById(id);
        if (bookDto.isPresent()) {
            request.setAttribute("book", bookDto.get());
            return "/WEB-INF/book.jsp";
        }
        return "/WEB-INF/books.jsp";
    }
}
