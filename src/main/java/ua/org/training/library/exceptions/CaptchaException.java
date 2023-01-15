package ua.org.training.library.exceptions;

public class CaptchaException extends Exception {
    private final String message;

    public CaptchaException(String message) {
        this.message = message;
    }

    public CaptchaException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CaptchaException{" +
                "message='" + message + '\'' +
                '}';
    }
}
