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
import ua.org.training.library.dto.StatusDto;
import ua.org.training.library.service.StatusService;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.mapper.JSONMapper;

import java.util.List;

@Controller("/status")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private final StatusService statusService;
    private final JSONMapper jsonMapper;

    @Get("/librarian/order/{id}")
    @SneakyThrows
    public String getLibrarianOrder(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get librarian order");
        long id = Utility.getIdFromUri(request);
        List<StatusDto> statusDtoList = statusService.getNextStatusesByOrderId(id);
        jsonMapper.toJson(response.getWriter(), statusDtoList);
        return "";
    }
}
