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
import ua.org.training.library.dto.AuthorDto;
import ua.org.training.library.dto.AuthorManagementDto;
import ua.org.training.library.form.AuthorSaveFormValidationError;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.mapper.JSONMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.List;
import java.util.Optional;

@Controller("/authors")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorController {
    private final AuthorService authorService;
    private final JSONMapper jsonMapper;
    private final RequestParamsObjectMapper requestParamsObjectMapper;

    @Get("/admin/list")
    @SneakyThrows
    public String getAuthorsAdmin(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get authors admin");
        String search = request.getParameter("query");
        List<AuthorManagementDto> authorDtoPage = authorService.searchAuthors(search);
        log.info("Authors: {}", authorDtoPage);
        jsonMapper.toJson(response.getWriter(), authorDtoPage);
        return "";
    }

    @Get("/admin")
    @SneakyThrows
    public String getAuthorsPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get authors page");
        Pageable pageable = requestParamsObjectMapper.getPageable(request);
        String search = request.getParameter("search");
        Page<AuthorManagementDto> authorDtoPage = authorService.searchAuthors(pageable, search);
        log.info("Authors: {}", authorDtoPage);
        jsonMapper.toJson(response.getWriter(), authorDtoPage);
        return "";
    }

    @Get("/admin/page")
    public String getAuthorsAdminPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get authors admin page");
        return "/WEB-INF/jsp/admin/authors.jsp";
    }

    @Post("/admin")
    @SneakyThrows
    public String createAuthor(HttpServletRequest request, HttpServletResponse response) {
        log.info("Create author");
        AuthorManagementDto authorDto = requestParamsObjectMapper.getAuthorDto(request);
        AuthorSaveFormValidationError author = authorService.createAuthor(authorDto);
        if (author.isContainsErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMapper.toJson(response.getWriter(), author);
            return "";
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        jsonMapper.toJson(response.getWriter(), authorDto);
        return "";
    }

    @Delete("/admin/{id}")
    @SneakyThrows
    public String deleteAuthor(HttpServletRequest request, HttpServletResponse response) {
        log.info("Delete author");
        long id = Utility.getIdFromUri(request);
        Optional<AuthorDto> author = authorService.deleteAuthor(id);
        if (author.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        jsonMapper.toJson(response.getWriter(), author.get());
        return "";
    }

    @Get("/admin/{id}/edit")
    public String getAuthorEditPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get author edit page");
        long id = Utility.getIdFromUri(request);
        Optional<AuthorManagementDto> author = authorService.getAuthor(id);
        if (author.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "redirect:library/authors/admin/page";
        }
        request.setAttribute("author", author.get());
        return "/WEB-INF/jsp/admin/edit-author.jsp";
    }

    @Put("/admin/{id}")
    public String updateAuthor(HttpServletRequest request, HttpServletResponse response) {
        log.info("Update author");
        AuthorManagementDto authorDto = requestParamsObjectMapper.getAuthorDto(request);
        AuthorSaveFormValidationError author = authorService.updateAuthor(authorDto);
        if (author.isContainsErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("author", authorDto);
            request.setAttribute("errors", author);
            return "/WEB-INF/jsp/admin/edit-author.jsp";
        }
        return "redirect:library/authors/admin/page";
    }
}
