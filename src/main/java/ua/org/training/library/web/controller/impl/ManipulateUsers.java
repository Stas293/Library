package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.service.RoleService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class ManipulateUsers implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(ManipulateUsers.class);
    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String[] uriDivided = Utility.getUriParts(request);
        long id;

        if (uriDivided.length == 2) {
            try {
                return returnJSONUsers(request, response);
            } catch (ServiceException e) {
                LOGGER.error("Error while getting users list", e);
                throw new ControllerException(e);
            }
        }
        id = Utility.parseLongOrDefault(uriDivided[2], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
        User user = null;
        try {
            user = userService.getUserById(id);
            session.setAttribute("account", user);
        } catch (ServiceException e) {
            LOGGER.error("Error while getting user by id", e);
        }
        if (uriDivided.length == 4 && uriDivided[3].equals("edit")) {
            String[] roles = user.getRoles()
                    .stream()
                    .map(Role::getCode)
                    .toArray(String[]::new);
            LOGGER.info(String.format("Role array : %s", Arrays.toString(roles)));
            session.setAttribute("roles", roles);
            try {
                session.setAttribute("rolesList", roleService.getAllRoles());
            } catch (ServiceException e) {
                LOGGER.error("Error while getting roles list", e);
                throw new ControllerException(e);
            }
            if (request.getMethod().equals("GET")) {
                return Links.USER_EDIT_PAGE;
            } else {
                LOGGER.debug(request.getParameterValues("role"));
                LOGGER.debug("Try to post my changed users roles!");
                Collection<Role> newRoles = new HashSet<>();
                for (String role : request.getParameterValues("role")) {
                    try {
                        newRoles.add(roleService.getRoleByCode(role));
                    } catch (ServiceException e) {
                        LOGGER.error("Error while getting role by code", e);
                        throw new ControllerException(e);
                    }
                    LOGGER.info(String.format("New roles : %s", newRoles));
                }
                user.setRoles(newRoles);
                try {
                    userService.updateUserRoles(user);
                } catch (ServiceException e) {
                    LOGGER.error("Error while updating user roles", e);
                    throw new ControllerException(e);
                }
            }
        }
        return Links.USER_INFO_PAGE;
    }

    private String returnJSONUsers(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        LOGGER.info("Try to get users list in JSON format");
        PageService<User> pageService = new PageService<>();
        Page<User> page = pageService.getPage(request);

        String jsonString = userService.getUserPage(page);
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error("Error while getting writer from response", e);
        }
        return "";
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
