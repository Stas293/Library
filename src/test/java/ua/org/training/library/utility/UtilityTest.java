package ua.org.training.library.utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void getApplicationProperty() {
        assertThrows(
                MissingResourceException.class,
                () -> Utility.getApplicationProperty("nonexistent"));
        String mysqlDriver = Utility.getApplicationProperty("mysql.driver");
        assertEquals("com.mysql.cj.jdbc.Driver", mysqlDriver);
    }

    @Test
    void tryParseDouble() {
        double result = Utility.tryParseDouble("1.0", 0);
        assertEquals(1.0, result);
        result = Utility.tryParseDouble("1", 0);
        assertEquals(1.0, result);
        result = Utility.tryParseDouble("1.0.0", 0);
        assertEquals(0.0, result);
    }

    @Test
    void parseLongOrDefault() {
        long result = Utility.parseLongOrDefault("1", 0);
        assertEquals(1, result);
        result = Utility.parseLongOrDefault("1.0", 0);
        assertEquals(0, result);
    }

    @Test
    void parseStringOrDefault() {
        String result = Utility.parseStringOrDefault("1", "0");
        assertEquals("1", result);
        result = Utility.parseStringOrDefault(null, "0");
        assertEquals("0", result);
    }

    @Test
    void getBundleInterface() {
        String result = Utility.getBundleInterface(Locale.ENGLISH, "user.login");
        assertEquals("Login:", result);
        assertThrows(
                MissingResourceException.class,
                () -> Utility.getBundleInterface(Locale.ENGLISH, "nonexistent"));
    }

    @Test
    void getLocaleFine() {
        String result = Utility.getLocaleFine(Locale.ENGLISH, 40);
        assertEquals("40.0 USD", result);
        result = Utility.getLocaleFine(Locale.ENGLISH, 0);
        assertEquals("0.0 USD", result);
        result = Utility.getLocaleFine(Locale.of("uk"), 40);
        assertEquals("1600.0 UAH", result);
        result = Utility.getLocaleFine(Locale.of("uk"), 0);
        assertEquals("0.0 UAH", result);
    }

    @Test
    void getLanguage() {
        String result = Utility.getLanguage(Locale.ENGLISH);
        assertEquals("en_UK", result);
        result = Utility.getLanguage(Locale.of("uk"));
        assertEquals("uk_UA", result);
    }

    @Test
    void getStringParameter() {
        String result = Utility.getStringParameter("1", "0");
        assertEquals("1", result);
        result = Utility.getStringParameter(null, "0");
        assertEquals("0", result);
    }

    @Test
    void getUriParts() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/library/user/login");
        String[] result = Utility.getUriParts(request);
        assertEquals("user", result[0]);
        assertEquals("login", result[1]);
        Mockito.when(request.getRequestURI()).thenReturn("/library/admin/books/4");
        result = Utility.getUriParts(request);
        assertEquals("admin", result[0]);
        assertEquals("books", result[1]);
        assertEquals("4", result[2]);
    }

    @Test
    void parseDateOrDefault() {
        Date result = Utility.parseDateOrDefault("2021-01-01", new Date(1));
        assertEquals(new Date(1609452000000L), result);
        result = Utility.parseDateOrDefault("2021-01-01 00:00:00", new Date(1));
        assertEquals(new Date(1609452000000L), result);
        result = Utility.parseDateOrDefault("2021-01-01 00:00:00.000", new Date(1));
        assertEquals(new Date(1609452000000L), result);
        result = Utility.parseDateOrDefault("2021-01-01 00:00:00.000000", new Date(1));
        assertEquals(new Date(1609452000000L), result);
        result = Utility.parseDateOrDefault("abc", new Date(1));
        assertEquals(new Date(1), result);
    }

    @Test
    void getLocale() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("locale")).thenReturn(Locale.ENGLISH);
        Locale result = Utility.getLocale(request);
        assertEquals(Locale.ENGLISH, result);
        Mockito.when(session.getAttribute("locale")).thenReturn(null);
        result = Utility.getLocale(request);
        assertEquals(Locale.ENGLISH, result);
    }

    @Test
    void tryParseInt() {
        int result = Utility.tryParseInt("1", 0);
        assertEquals(1, result);
        result = Utility.tryParseInt("1.0", 0);
        assertEquals(0, result);
    }

    @Test
    void generateCode() {
        String result = Utility.generateCode();
        assertEquals(36, result.length());
    }

    @Test
    void getParameters() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameterNames()).thenReturn(Collections.enumeration(List.of("a", "b")));
        Mockito.when(request.getParameter("a")).thenReturn("1");
        Mockito.when(request.getParameter("b")).thenReturn("2");
        String result = Utility.getParameters(request);
        assertEquals("a=1&b=2", result);
    }
}