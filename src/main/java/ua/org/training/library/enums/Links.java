package ua.org.training.library.enums;

public enum Links {
    LOGIN_PAGE("/WEB-INF/login.jsp"),
    REGISTRATION_PAGE("/WEB-INF/registration.jsp"),
    MAIN_PAGE_REDIRECT("redirect:"),
    LOGIN_PAGE_REDIRECT("redirect:library/login"),
    USER_PAGE_REDIRECT("redirect:library/order/user/page"),
    FORGOT_PASSWORD_PAGE("/WEB-INF/reset-password.jsp"),
    RESET_PASSWORD_PAGE_REDIRECT("redirect:library/forgot-password"),
    RESET_PASSWORD_PAGE("/WEB-INF/reset-password.jsp"),
    ENTER_NEW_PASSWORD_PAGE("/WEB-INF/enter-new-password.jsp"),
    ;

    private final String link;

    Links(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
