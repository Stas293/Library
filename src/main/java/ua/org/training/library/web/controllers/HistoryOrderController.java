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
import ua.org.training.library.dto.HistoryOrderDto;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.HistoryOrderService;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.mapper.JSONMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;

@Controller("/history")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryOrderController {
    private final HistoryOrderService historyOrderService;
    private final SecurityService securityService;
    private final RequestParamsObjectMapper requestParamsObjectMapper;
    private final JSONMapper jsonMapper;

    @Get("/user/page")
    public String getUser(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get user");
        return "/WEB-INF/jsp/user/history.jsp";
    }

    @Get("/user")
    @SneakyThrows
    public String getHistory(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get history");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        Pageable pageable = requestParamsObjectMapper.getPageable(request);
        String search = request.getParameter("search");
        Locale locale = Utility.getLocale(request);
        Page<HistoryOrderDto> historyOrderDtoPage = historyOrderService.getPageByUser(pageable, authorityUser, search, locale);
        jsonMapper.toJson(response.getWriter(), historyOrderDtoPage);
        return "";
    }
}
