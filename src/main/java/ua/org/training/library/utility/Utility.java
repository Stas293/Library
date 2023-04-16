package ua.org.training.library.utility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
@UtilityClass
public class Utility {
    private static final String APP_PROPERTIES_NAME = "application";


    public static double tryParseDouble(String applicationProperty, int defaultValue) {
        try {
            return Double.parseDouble(applicationProperty);
        } catch (NumberFormatException e) {
            log.error( String.format("Number format exception %s", applicationProperty));
            log.info( String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static int tryParseInt(String applicationProperty, int defaultValue) {
        try {
            return Integer.parseInt(applicationProperty);
        } catch (NumberFormatException e) {
            log.error( String.format("Number format exception %s", applicationProperty));
            log.info( String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static long parseLongOrDefault(String applicationProperty, long defaultValue) {
        try {
            return Long.parseLong(applicationProperty);
        } catch (NumberFormatException e) {
            log.error( String.format("Number format exception %s", applicationProperty));
            log.info( String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static String parseStringOrDefault(String applicationProperty, String defaultValue) {
        return applicationProperty == null ? defaultValue : applicationProperty;
    }

    public static String getBundleInterface(Locale locale, String bundleValue) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                Values.BUNDLE_NAME,
                locale);
        return bundle.getString(bundleValue);
    }

    public static String getLocaleFine(Locale locale, double fine) {
        double exchangeRate = Utility.tryParseDouble(
                Utility.getBundleInterface(
                        locale,
                        Values.BUNDLE_CURRENCY_EXCHANGE),
                Values.DEFAULT_CURRENCY_EXCHANGE);
        return fine * exchangeRate + " " + Utility.getBundleInterface(locale, Values.BUNDLE_CURRENCY);
    }

    public static String getLanguage(Locale locale) {
        return getBundleInterface(
                locale,
                Values.BUNDLE_LANGUAGE_FOR_BOOK);
    }

    public static String getStringParameter(String getProperty, String defaultProperty) {
        return Optional.ofNullable(getProperty).orElse(defaultProperty);
    }

    public static Locale getLocale(HttpServletRequest request) {
        return Locale.of(
                parseStringOrDefault(
                        (String) request.getSession()
                                .getAttribute(Values.APP_LANG_ATTRIBUTE),
                        Values.APP_DEFAULT_LANGUAGE));
    }

    public static long getIdFromUri(HttpServletRequest request) {
        String[] uriParts = request.getRequestURI().split("/");
        return parseLongOrDefault(uriParts[uriParts.length - 1], Values.DEFAULT_ID);
    }

    public static Date parseDateOrDefault(String parameter, Date defaultValue) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(parameter);
        } catch (ParseException | NullPointerException e) {
            log.error( String.format("Date format exception %s", parameter), e);
        }
        return defaultValue;
    }
}
