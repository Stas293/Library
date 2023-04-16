package ua.org.training.library.web.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Controller;

@Controller("/historyOrder")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryOrderController {
}
