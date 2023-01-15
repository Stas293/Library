package ua.org.training.library.exceptions;

public class MapException extends RuntimeException {
    private final String message;

    public MapException(String message) {
        this.message = message;
    }

    public MapException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
