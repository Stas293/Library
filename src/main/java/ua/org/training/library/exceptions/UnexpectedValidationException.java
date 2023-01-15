package ua.org.training.library.exceptions;

public class UnexpectedValidationException extends Exception {
    private final String message;

    public UnexpectedValidationException(String message) {
        this.message = message;
    }

    public UnexpectedValidationException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "UnexpectedValidationException{" +
                "message='" + message + '\'' +
                '}';
    }
}
