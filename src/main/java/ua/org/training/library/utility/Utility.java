package ua.org.training.library.utility;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                Locale.of(APP_DEFAULT_LANGUAGE));
        return bundle.getString(property);
    }


    public static double tryParseDouble(String applicationProperty, int defaultValue) {
        try {
            return Double.parseDouble(applicationProperty);
        } catch (NumberFormatException e) {
            LOGGER.error( String.format("Number format exception %s", applicationProperty));
            LOGGER.info( String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static int tryParseInt(String applicationProperty, int defaultValue) {
        try {
            return Integer.parseInt(applicationProperty);
        } catch (NumberFormatException e) {
            LOGGER.error( String.format("Number format exception %s", applicationProperty));
            LOGGER.info( String.format("Setting default value %s", defaultValue));
        }
        return defaultValue;
    }

    public static long parseLongOrDefault(String applicationProperty, long defaultValue) {
        try {
            return Long.parseLong(applicationProperty);
        } catch (NumberFormatException e) {
            LOGGER.error( String.format("Number format exception %s", applicationProperty));
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
        double exchangeRate = Utility.tryParseDouble(
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

    public static Date parseDateOrDefault(String parameter, Date defaultValue) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(parameter);
        } catch (ParseException | NullPointerException e) {
            LOGGER.error( String.format("Date format exception %s", parameter), e);
        }
        return defaultValue;
    }

    public static Locale getLocale(HttpServletRequest request) {
        return Locale.of(
                parseStringOrDefault(
                        (String) request.getSession()
                                .getAttribute(Constants.RequestAttributes.APP_LANG_ATTRIBUTE),
                APP_DEFAULT_LANGUAGE));
    }

    public static String sendRequest(String url, String requestMethod) {
        try {
            URL urlObj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.flush();
            dataOutputStream.close();
            StringBuilder response = collectStringFromResponse(connection);
            return response.toString();
        } catch (MalformedURLException e) {
            LOGGER.error( String.format("Malformed URL exception %s", url), e);
        } catch (IOException e) {
            LOGGER.error( String.format("IO exception %s", url), e);
        }
        return null;
    }

    private static StringBuilder collectStringFromResponse(HttpsURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        Scanner scanner = new Scanner(connection.getInputStream());
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        return response;
    }

    public static String generateCode() {
        return UUID.randomUUID().toString();
    }

    public static String getParameters(HttpServletRequest request) {
        StringBuilder parameters = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameters.append(parameterName).append("=").append(request.getParameter(parameterName)).append("&");
        }
        parameters.deleteCharAt(parameters.length() - 1);
        return parameters.toString();
    }
}
