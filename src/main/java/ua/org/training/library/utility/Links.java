package ua.org.training.library.utility;

public class Links {
    public static final String ADMIN_CONTROL_PAGE = "/WEB-INF/jsp/admin/page.jsp";
    public static final String LIBRARIAN_ORDER_UPDATE_SUCCESS = "redirect:library/librarian/page?updated=true";
    public static final String LIBRARIAN_PAGE = "/WEB-INF/jsp/librarian/page.jsp";
    public static final String USER_PAGE_REDIRECT_NEW_ORDER_CREATED = "redirect:library/user/page?created=true";
    public static final String REGISTRATION_PAGE = "/WEB-INF/registration.jsp";
    public static final String REGISTRATION_FORM_SUCCESS = "redirect:library/login";
    public static final String LOGIN_PAGE = "/WEB-INF/login.jsp";
    public static final String ADMIN_PAGE_REDIRECT = "redirect:library/admin/page";
    public static final String LIBRARIAN_PAGE_REDIRECT = "redirect:library/librarian/page";
    public static final String USER_PAGE_REDIRECT = "redirect:library/user/page";
    public static final String MAIN_PAGE = "redirect:";
    public static final String ACCESS_DENIED_PAGE = "WEB-INF/access-denied.jsp";
    public static final String USER_EDIT_PAGE = "/WEB-INF/jsp/admin/edit-users.jsp";
    public static final String USER_INFO_PAGE = "/WEB-INF/jsp/admin/user.jsp";
    public static final String ADMIN_PAGE_REDIRECT_DELETE_ACCOUNT_FAILED = "redirect:library/admin/page?deleted=false";
    public static final String ADMIN_PAGE_REDIRECT_DELETE_ACCOUNT_SUCCESS = "redirect:library/admin/page?deleted=true";
    public static final String USER_PAGE = "/WEB-INF/jsp/user/page.jsp";
    public static final String BOOK_PAGE = "/WEB-INF/book.jsp";
    public static final String USER_PAGE_REDIRECT_NEW_ORDER_NOT_CREATED = "redirect:library/user/page?created=false";
    public static final String PERSONAL_DATA = "/WEB-INF/jsp/personal-data.jsp";
    public static final String USER_HISTORY_PAGE = "/WEB-INF/jsp/user/history.jsp";
    public static final String ADMIN_PAGE_REDIRECT_DISABLE_ACCOUNT_FAILED = "redirect:library/admin/page?disabled=false";
    public static final String ADMIN_PAGE_REDIRECT_DISABLE_ACCOUNT_SUCCESS = "redirect:library/admin/page?disabled=true";
    public static final String ADMIN_PAGE_REDIRECT_ENABLE_ACCOUNT_FAILED = "redirect:library/admin/page?enabled=false";
    public static final String ADMIN_PAGE_REDIRECT_ENABLE_ACCOUNT_SUCCESS = "redirect:library/admin/page?enabled=true";
    public static final String ADMIN_BOOKS_PAGE = "/WEB-INF/jsp/admin/books.jsp";
    public static final String ADMIN_AUTHORS_PAGE = "/WEB-INF/jsp/admin/authors.jsp";
    public static final String ADMIN_ADD_BOOK = "/WEB-INF/jsp/admin/add-book.jsp";
    public static final String ADMIN_AUTHORS_PAGE_CREATE_ERROR = "redirect:library/admin/authors?created=false";
    public static final String ADMIN_AUTHORS_PAGE_CREATE_SUCCESS = "redirect:library/admin/authors?created=true";
    public static final String ADMIN_AUTHOR_EDIT = "/WEB-INF/jsp/admin/edit-author.jsp";
    public static final String ADMIN_BOOKS_PAGE_CREATE_ERROR = "redirect:library/admin/books?created=false";
    public static final String ADMIN_BOOKS_PAGE_CREATE_SUCCESS = "redirect:library/admin/books?created=true";
    public static final String ADMIN_BOOK_EDIT = "/WEB-INF/jsp/admin/edit-book.jsp";
    public static final String ADMIN_BOOKS_PAGE_UPDATE_ERROR = "redirect:library/admin/books?updated=false";
    public static final String ADMIN_BOOKS_PAGE_UPDATE_SUCCESS = "redirect:library/admin/books?updated=true";
    public static final String ADMIN_BOOKS_PAGE_DELETE_ERROR = "redirect:library/admin/books?deleted=false";
    public static final String ADMIN_BOOKS_PAGE_DELETE_SUCCESS = "redirect:library/admin/books?deleted=true";
    public static final String ADMIN_AUTHORS_PAGE_DELETE_SUCCESS = "redirect:library/admin/authors?deleted=true";
    public static final String ADMIN_AUTHORS_PAGE_DELETE_ERROR = "redirect:library/admin/authors?deleted=false";
    public static final String ERROR_PAGE = "redirect:library/error";
    public static final String ADMIN_AUTHORS_PAGE_UPDATE_ERROR = "redirect:library/admin/authors?saved=false";
    public static final String LIBRARIAN_ORDER_UPDATE_FAILED = "redirect:library/librarian/page?updated=false";
    public static final String EDIT_PERSONAL_DATA_PAGE = "/WEB-INF/jsp/edit-personal.jsp";
    public static final String ERROR_CHANGE_PERSONAL_DATA_PAGE = "redirect:library/personal-data?edit_page=false";
    public static final String ERROR_EDIT_PERSONAL_DATA = "redirect:library/personal-data?edit=false";
    public static final String EDIT_PERSONAL_DATA_SUCCESS_PAGE = "redirect:library/personal-data?edit=true";
    public static final String PASSWORD_EDIT_PAGE = "/WEB-INF/jsp/edit-password.jsp";
    public static final String PASSWORD_EDIT_PAGE_FAIL = "redirect:library/personal-data?password=false";
    public static final String ADMIN_USER_PAGE_REDIRECT = "redirect:library/admin/page";

    private Links() {
    }
}
