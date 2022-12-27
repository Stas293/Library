package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.SecurityDao;
import ua.org.training.library.exceptions.JDBCException;

import static org.junit.jupiter.api.Assertions.*;

class JDBCSecurityDaoTest {
    SecurityDao securityDao;
    @BeforeEach
    void setUp() throws JDBCException {
        securityDao = DaoFactory.getInstance().createSecurityDao();
    }

    @Test
    void getPasswordByLogin() throws JDBCException {
        String password = securityDao.getPasswordByLogin("Test");
        assertEquals("Test", password);
        securityDao = DaoFactory.getInstance().createSecurityDao();
        password = securityDao.getPasswordByLogin("kwejhgkjwhgkjweh");
        assertNull(password);
    }
}