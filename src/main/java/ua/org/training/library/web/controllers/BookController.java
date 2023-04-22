package ua.org.training.library.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Controller;
import ua.org.training.library.context.annotations.mapping.Delete;
import ua.org.training.library.context.annotations.mapping.Get;
import ua.org.training.library.context.annotations.mapping.Post;
import ua.org.training.library.context.annotations.mapping.Put;
import ua.org.training.library.dto.BookChangeDto;
import ua.org.training.library.dto.BookDto;
import ua.org.training.library.form.BookChangeFormValidationError;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.mapper.JSONMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;
import java.util.Optional;

@Controller("/books")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookController {
    private final BookService bookService;
    private final RequestParamsObjectMapper requestParamsObjectMapper;
    private final JSONMapper jsonMapper;

    @Get
    public String getBooks(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get books");
        return "/WEB-INF/books.jsp";
    }

    @Get("/page")
    @SneakyThrows
    public String getBookPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get books page");
        Pageable pageable = requestParamsObjectMapper.getPageable(request);
        String search = request.getParameter("search");
        Locale locale = Utility.getLocale(request);
        Page<BookDto> bookDtoPage = bookService.searchBooks(pageable, locale, search);
        jsonMapper.toJson(response.getWriter(), bookDtoPage);
        return "";
    }

    @Get("/{id}")
    public String getBookById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get book by id");
        long id = Utility.getIdFromUri(request);
        Locale locale = request.getLocale();
        Optional<BookDto> bookDto = bookService.getBookById(id, locale);
        if (bookDto.isPresent()) {
            request.setAttribute("book", bookDto.get());
            return "/WEB-INF/book.jsp";
        }
        return "redirect:library/books";
    }

    @Get("/admin/page")
    public String getAdminBookPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get admin books page");
        return "/WEB-INF/jsp/admin/books.jsp";
    }

    @Get("/admin/{id}")
    public String getAdminBookById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get admin book by id");
        long id = Utility.getIdFromUri(request);
        Locale locale = Utility.getLocale(request);
        Optional<BookDto> bookDto = bookService.getBookById(id, locale);
        if (bookDto.isPresent()) {
            request.setAttribute("book", bookDto.get());
            return "/WEB-INF/jsp/admin/book.jsp";
        }
        return "redirect:library/books/admin/page";
    }

    @Get("/admin/create")
    public String createBook(HttpServletRequest request, HttpServletResponse response) {
        log.info("Create book");
        return "/WEB-INF/jsp/admin/add-book.jsp";
    }

    @Get("/admin/{id}/edit")
    public String editBook(HttpServletRequest request, HttpServletResponse response) {
        log.info("Edit book");
        long id = Utility.getIdFromUri(request);
        Locale locale = Utility.getLocale(request);
        Optional<BookChangeDto> bookDto = bookService.getBookChangeById(locale, id);
        if (bookDto.isPresent()) {
            request.setAttribute("book", bookDto.get());
            return "/WEB-INF/jsp/admin/edit-book.jsp";
        }
        return "redirect:library/books/admin/page";
    }

    @Post("/admin")
    public String saveBook(HttpServletRequest request, HttpServletResponse response) {
        log.info("Save book");
        BookChangeDto bookDto = requestParamsObjectMapper.getBookChangeDto(request);
        Locale locale = Utility.getLocale(request);
        BookChangeFormValidationError bookChangeFormValidationError = bookService.saveBook(locale, bookDto);
        if (bookChangeFormValidationError.isContainsErrors()) {
            request.setAttribute("book", bookService.getBookChangeByBookChangeDto(locale, bookDto).get());
            request.setAttribute("bookValidationError", bookChangeFormValidationError);
            return "/WEB-INF/jsp/admin/add-book.jsp";
        }
        return "redirect:library/books/admin/page";
    }

    @Put("/admin/{id}/edit")
    public String updateBook(HttpServletRequest request, HttpServletResponse response) {
        log.info("Update book");
        BookChangeDto bookDto = requestParamsObjectMapper.getBookChangeDto(request);
        Locale locale = Utility.getLocale(request);
        BookChangeFormValidationError bookChangeFormValidationError = bookService.updateBook(locale, bookDto);
        if (bookChangeFormValidationError.isContainsErrors()) {
            request.setAttribute("book", bookService.getBookChangeByBookChangeDto(locale, bookDto).get());
            request.setAttribute("bookValidationError", bookChangeFormValidationError);
            return "/WEB-INF/jsp/admin/edit-book.jsp";
        }
        return "redirect:library/books/admin/page";
    }

    @Delete("/admin/{id}")
    public String deleteBook(HttpServletRequest request, HttpServletResponse response) {
        log.info("Delete book");
        long id = Utility.getIdFromUri(request);
        Optional<BookDto> bookDto = bookService.deleteBook(id);
        return bookDto.map(dto -> "redirect:library/books/admin/page")
                .orElseGet(() -> "redirect:library/books/admin/" + id);
    }
}
