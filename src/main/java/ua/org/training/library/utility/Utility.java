package ua.org.training.library.utility;


import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.enums.DefaultValues;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class Utility {
    private static final Pattern numberPattern = Pattern.compile("\\d+");


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
}
