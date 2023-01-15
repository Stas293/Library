package ua.org.training.library.exceptions;

public class DaoException extends RuntimeException {
    private final String message;

    public DaoException(String message) {
        this.message = message;
    }

    public DaoException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DaoException{" +
                "message='" + message + '\'' +
                '}';
    }
}

