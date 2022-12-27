package ua.org.training.library.exceptions;

public class JDBCException extends Exception {
    private String message;

    public JDBCException(String message) {
        this.message = message;
    }

    public JDBCException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public JDBCException(Throwable cause) {
        super(cause);
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
