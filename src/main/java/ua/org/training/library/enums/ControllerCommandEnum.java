package ua.org.training.library.enums;

import ua.org.training.library.web.controller.ControllerCommand;
import ua.org.training.library.web.controller.impl.*;

public enum ControllerCommandEnum {
    LOGIN("login", new Login()),
    LOGOUT("logout", new Logout()),
    BOOKS_PAGE("booksPage", new BooksPagination()),
    BOOKS("books", new Books()),
    ACCESS_DENIED("accessDenied", new AccessDenied()),
    ADMIN_USERS("adminUsers", new ManipulateUsers()),
    ADMIN_PAGE("adminPage", new AdminControls()),
    ADMIN_DELETE_USER("deleteUser", new DeleteUser()),
    ADMIN_DISABLE_USER("disableUser", new DisableUser()),
    ADMIN_ENABLE_USER("enableUser", new EnableUser()),
    ADMIN_BOOKS("adminBooks", new AdminBooks()),
    ADMIN_AUTHORS("adminAuthors", new AdminAuthors()),
    ADMIN_AUTHORS_PAGE("adminAuthorsList", new AdminAuthorsPage()),
    ADMIN_AUTHOR("adminAuthor", new AdminManipulateAuthor()),
    ADMIN_NEW_BOOK("adminNewBook", new AdminNewBook()),
    ADMIN_BOOK("adminBook", new AdminManipulateBook()),
    ADMIN_ADD_BOOK("adminAddBook", new AdminAddBook()),
    ADMIN_NEW_AUTHOR("adminNewAuthor", new AdminNewAuthor()),
    EXCEPTION("exception", new ThrowException()),
    LIBRARIAN_EDIT_ORDER("librarianEditOrder", new LibrarianEditOrder()),
    LIBRARIAN_PAGE("librarianPage", new LibrarianPage()),
    LIBRARIAN_ORDER("librarianOrder", new LibrarianOrder()),
    USER_NEW_ORDER("userNewOrder", new NewOrder()),
    REGISTRATION("register", new Register()),
    USER_HISTORY("history", new UserHistoryPage()),
    USER_ORDER_HISTORY("userOrderHistory", new UserOrderHistory()),
    USER_PAGE("userPage", new UserPage()),
    USER_ORDER("userOrders", new UserMakeOrder()),
    BOOKS_TO_ORDER("booksToOrder", new BooksToOrder()),
    GET_PLACES("getPlaces", new GetPlaces()),
    PERSONAL_DATA("personalData", new PersonalData()),;

    private final String command;
    private final ControllerCommand controllerCommand;

    ControllerCommandEnum(String command, ControllerCommand controllerCommand) {
        this.command = command;
        this.controllerCommand = controllerCommand;
    }

    public String getCommand() {
        return command;
    }

    public ControllerCommand getControllerCommand() {
        return controllerCommand;
    }
}
