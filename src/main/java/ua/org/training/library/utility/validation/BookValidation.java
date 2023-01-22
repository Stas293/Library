package ua.org.training.library.utility.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.exceptions.UnexpectedValidationException;
import ua.org.training.library.form.BookValidationError;
import ua.org.training.library.model.Book;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.Constants;

import java.util.*;
import java.util.regex.Pattern;

public class BookValidation {
    private static final Logger LOGGER = LogManager.getLogger(BookValidation.class);
    private final BookService bookService;

    public BookValidation(BookService bookService) {
        this.bookService = bookService;
    }

    public void validation(Locale locale, Book book, BookValidationError errors) throws UnexpectedValidationException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_NAME, locale);

        validateNameLength(book.getName(), errors);
        validateISBNLength(book.getISBN(), errors);

        validateNamePattern(
                book.getName(),
                bundle.getString(Constants.BundleStrings.APP_BOOK_NAME_PATTERN),
                errors);
        validateISBNPattern(
                book.getISBN(),
                bundle.getString(Constants.BundleStrings.APP_ISBN_PATTERN),
                errors);

        validateISBNCollision(
                book.getISBN(),
                errors,
                book.getId());

        validateCount(book.getCount(), errors);
        validatePublicationDate(book.getPublicationDate(), errors);
        validateFine(book.getFine(), errors);
    }

    private boolean notValidFieldLength(String field, int min, int max) {
        return field.length() < min || field.length() > max;
    }

    private boolean notEqualFieldLength(String field, int length) {
        return field.length() != length;
    }

    private void validateNameLength(String name, BookValidationError errors) {
        if (notValidFieldLength(name.trim(), Constants.MIN_THREE, Constants.MAX_255)) {
            errors.setName(Constants.Validation.APP_NAME_LENGTH_ERROR);
        }
    }

    private void validateISBNLength(String ISBN, BookValidationError errors) {
        if (notEqualFieldLength(ISBN, Constants.ISBN_LENGTH)) {
            errors.setISBN(Constants.Validation.APP_ISBN_LENGTH_ERROR);
        }
    }

    private void validateNamePattern(String name, String pattern, BookValidationError errors) {
        Pattern pattern1 = Pattern.compile(pattern);
        if (!pattern1.matcher(name).matches()) {
            errors.setName(Constants.Validation.APP_NAME_PATTERN_ERROR);
        }
    }

    private void validateISBNPattern(String ISBN, String pattern, BookValidationError errors) {
        Pattern pattern1 = Pattern.compile(pattern);
        if (!pattern1.matcher(ISBN).matches()) {
            errors.setISBN(Constants.Validation.APP_ISBN_PATTERN_ERROR);
        }
    }

    private void validateCount(int count, BookValidationError errors) {
        if (count < Constants.MIN_ONE) {
            errors.setCount(Constants.Validation.APP_COUNT_ERROR);
        }
    }

    private void validatePublicationDate(Date publicationDate, BookValidationError errors) {
        if (publicationDate == null) {
            errors.setDatePublication(Constants.Validation.APP_PUBLICATION_DATE_ERROR);
        }
    }

    private void validateFine(double fine, BookValidationError errors) {
        if (fine < Constants.MIN_ZERO) {
            errors.setFinePerDay(Constants.Validation.APP_FINE_ERROR);
        }
    }

    private void validateISBNCollision(String ISBN, BookValidationError errors, long bookId) throws UnexpectedValidationException {
        try {
            if (bookService.getBookByISBN(ISBN).getId() == bookId)
                return;
        } catch (ServiceException e) {
            LOGGER.error(String.format("Error while getting book by ISBN: %s", ISBN), e);
            return;
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("Error while getting book by ISBN: %s", ISBN), e);
            throw new UnexpectedValidationException(e.getMessage(), e);
        }
        errors.setISBN(Constants.Validation.APP_ISBN_COLLISION_ERROR);
    }

    public void validateAuthorIds(List<String> authorIds, BookValidationError errors) {
        if (authorIds == null || authorIds.isEmpty() || authorIds.contains("")) {
            errors.setAuthors(Constants.Validation.APP_AUTHORS_ERROR);
        }
    }
}
