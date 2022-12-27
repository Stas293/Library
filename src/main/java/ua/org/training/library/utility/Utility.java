package ua.org.training.library.utility;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import static ua.org.training.library.utility.Constants.APP_DEFAULT_LANGUAGE;
import static ua.org.training.library.utility.Constants.BUNDLE_LANGUAGE_FOR_BOOK;

public class Utility {

    private Utility() {
    }

    private static final String APP_PROPERTIES_NAME = "application";

    private static final Logger LOGGER = LogManager.getLogger(Utility.class);

    public static String getApplicationProperty(String property) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                APP_PROPERTIES_NAME,
                new Locale(APP_DEFAULT_LANGUAGE));
        return bundle.getString(property);
    }


    public static int tryParseInteger(String applicationProperty, int defaultValue) {
        try {
            return Integer.parseInt(applicationProperty);
        } catch (NumberFormatException e) {
            LOGGER.error( String.format("Number format exception %s", applicationProperty), e);
            LOGGER.info( String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static long parseLongOrDefault(String applicationProperty, long defaultValue) {
        try {
            return Long.parseLong(applicationProperty);
        } catch (NumberFormatException e) {
            LOGGER.error( String.format("Number format exception %s", applicationProperty), e);
            LOGGER.info( String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static String parseStringOrDefault(String applicationProperty, String defaultValue) {
        return applicationProperty == null ? defaultValue : applicationProperty;
    }

    public static String getBundleInterface(Locale locale, String bundleValue) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.BUNDLE_NAME,
                locale);
        return bundle.getString(bundleValue);

    }

    public static String getLocaleFine(Locale locale, double fine) {
        int exchangeRate = Utility.tryParseInteger(
                Utility.getBundleInterface(
                        locale,
                        Constants.BUNDLE_CURRENCY_EXCHANGE),
                Constants.DEFAULT_CURRENCY_EXCHANGE);
        return fine * exchangeRate + " " + Utility.getBundleInterface(locale, Constants.BUNDLE_CURRENCY);
    }

    public static String getLanguage(Locale locale) {
        return getBundleInterface(
                locale,
                BUNDLE_LANGUAGE_FOR_BOOK);
    }

    public static String getStringParameter(String getProperty, String defaultProperty) {
        return Optional.ofNullable(getProperty).orElse(defaultProperty);
    }

    public static String[] getUriParts(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceAll(Constants.APP_PATH_REG_EXP, Constants.APP_STRING_DEFAULT_VALUE);
        return path.split("/");
    }

    public static Date parseDateOrDefault(String parameter, Date dateExpire) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(parameter);
        } catch (ParseException e) {
            LOGGER.error( String.format("Date format exception %s", parameter), e);
        }
        return dateExpire;
    }

    public static Locale getLocale(HttpServletRequest request) {
        return new Locale(parseStringOrDefault((String) request.getSession().getAttribute(Constants.RequestAttributes.APP_LANG_ATTRIBUTE),
                APP_DEFAULT_LANGUAGE));
    }
}
