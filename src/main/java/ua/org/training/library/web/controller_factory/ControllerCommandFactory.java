package ua.org.training.library.web.controller_factory;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.enums.ControllerCommandEnum;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerCommandFactory {
    private static final Logger LOGGER = LogManager.getLogger(ControllerCommandFactory.class);
    private static final Set<ControllerCommandEnum> commands =
            Arrays.stream(
                    ControllerCommandEnum.values())
                    .collect(Collectors.toSet());

    private ControllerCommandFactory() {
    }

    public static ControllerCommand getCommand(String requestURI) {
        LOGGER.info(requestURI);
        String path = requestURI.replaceAll(Constants.APP_PATH_REG_EXP, "");

        if (path.contains("admin/users")) path = "admin/users";
        else if (path.contains("admin/author/")) {
            path = "admin/author";
        } else if (path.contains("admin/book/")) {
            path = "admin/book";
        }
        LOGGER.info(path);

        path = path.replaceAll("/", "_").replaceAll("-", "_").toUpperCase();

        String finalPath = path;
        return commands
                .stream()
                .filter(c -> c.name().equalsIgnoreCase(finalPath))
                .findFirst()
                .orElse(ControllerCommandEnum.LOGIN)
                .getControllerCommand();
    }
}
