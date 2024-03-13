package ua.org.training.library.utility;


import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.enums.DefaultValues;
import ua.org.training.library.exceptions.ValidationException;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.utility.page.impl.Sort;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class Utility {
    private static final Pattern numberPattern = Pattern.compile("\\d+");

    public static Type[] getTypeParametersFromString(String typeParameters) {
        String types = typeParameters.substring(typeParameters.indexOf("<") + 1, typeParameters.indexOf(">"));
        return Arrays.stream(types.split(","))
                .map(String::trim)
                .map(Utility::getType)
                .toArray(Type[]::new);
    }

    private static Type getType(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException e) {
            log.error(String.format("Class not found exception %s", s));
        }
        return null;
    }

    public String getEnvOrProperty(String s) {
        if (s.startsWith("${") && s.endsWith("}")) {
            String env = s.substring(2, s.length() - 1);
            return Optional.ofNullable(System.getenv(env)).orElse(s);
        }
        return s;
    }

    public void throwExceptionWithoutMessage(Annotation annotation) {
        throw new ValidationException("Validation failed for " + annotation.annotationType().getSimpleName());
    }

    @SneakyThrows
    public void throwExceptionWithMessage(Method methodMessage, Annotation annotation) {
        String message = (String) methodMessage.invoke(annotation);
        throw new ValidationException(message);
    }

    public static double tryParseDouble(String applicationProperty, int defaultValue) {
        try {
            return Double.parseDouble(applicationProperty);
        } catch (NumberFormatException e) {
            log.error(String.format("Number format exception %s", applicationProperty));
            log.info(String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static int tryParseInt(String applicationProperty, int defaultValue) {
        try {
            return Integer.parseInt(applicationProperty);
        } catch (NumberFormatException e) {
            log.error(String.format("Number format exception %s", applicationProperty));
            log.info(String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static long parseLongOrDefault(String applicationProperty, long defaultValue) {
        try {
            return Long.parseLong(applicationProperty);
        } catch (NumberFormatException e) {
            log.error(String.format("Number format exception %s", applicationProperty));
            log.info(String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static String parseStringOrDefault(String applicationProperty, String defaultValue) {
        return applicationProperty == null ? defaultValue : applicationProperty;
    }

    public static String getBundleInterface(Locale locale, String bundleValue) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                DefaultValues.BUNDLE_NAME.getValue(),
                locale);
        return bundle.getString(bundleValue);
    }

    public static double getLocaleFine(Locale locale, double fine) {
        double exchangeRate = Double.parseDouble(
                Utility.getBundleInterface(
                        locale,
                        DefaultValues.BUNDLE_CURRENCY_EXCHANGE.getValue()));
        return Math.round(fine * exchangeRate * 100.0) / 100.0;
    }

    public static String getStringParameter(String getProperty, String defaultProperty) {
        return Optional.ofNullable(getProperty).orElse(defaultProperty);
    }

    public static Locale getLocale(HttpServletRequest request) {
        return Locale.of(
                parseStringOrDefault(
                        (String) request.getSession()
                                .getAttribute(DefaultValues.APP_LANG_ATTRIBUTE.getValue()),
                        DefaultValues.APP_DEFAULT_LANGUAGE.getValue()));
    }

    public static long getIdFromUri(HttpServletRequest request) {
        return Long.parseLong(numberPattern.matcher(request.getRequestURI()).results()
                .map(MatchResult::group)
                .findFirst().orElse("-1"));

    }

    public static Book mapBookFine(Order order) {
        log.info("Map order to order dto");
        log.info("Order: {}", order);
        Book book = order.getBook();
        if (order.getDateExpire() != null) {
            log.info("Order date expire: {}", order.getDateExpire());
            if (order.getDateExpire().isAfter(LocalDate.now())) {
                log.info("Order date expire is before today");
                book.setFine(0);
            } else {
                log.info("Order date expire is after today");
                int daysToFine = (int) (LocalDate.now().toEpochDay() - order.getDateExpire().toEpochDay());
                book.setFine(book.getFine() * daysToFine);
            }
        }
        return book;
    }

    public String orderByMinMax(Sort sort, Sort defaultSort) {
        if (sort == null) {
            sort = defaultSort;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Sort.Order> sortIterator = sort.iterator();
        while (sortIterator.hasNext()) {
            Sort.Order order = sortIterator.next();
            if (order.getDirection().equals(Sort.Direction.ASC)) {
                stringBuilder.append("MIN(");
            } else {
                stringBuilder.append("MAX(");
            }
            stringBuilder.append(order.getProperty()).append(")");
            if (order.getDirection().equals(Sort.Direction.DESC)) {
                stringBuilder.append(" DESC");
            }
            if (sortIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public String orderBy(Sort sort, Sort defaultSort) {
        if (sort == null) {
            sort = defaultSort;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Sort.Order> sortIterator = sort.iterator();
        while (sortIterator.hasNext()) {
            Sort.Order order = sortIterator.next();
            stringBuilder.append(order.getProperty()).append(" ").append(order.getDirection());
            if (sortIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static double delocalizeFine(Locale locale, double fine) {
        double exchangeRate = Double.parseDouble(
                Utility.getBundleInterface(
                        locale,
                        DefaultValues.BUNDLE_CURRENCY_EXCHANGE.getValue()));
        return Math.round(fine / exchangeRate * 100.0) / 100.0;
    }

    public static Long tryParseLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            log.error(String.format("Number format exception %s", id));
        }
        return null;
    }
}
