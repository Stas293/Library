package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.SecurityDao;
import ua.org.training.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCSecurityDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private SecurityDao securityDao;

    @Test
    void getPasswordByLogin() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("password");
        securityDao = new JDBCSecurityDao(connection);
        assertEquals("password", securityDao.getPasswordByLogin("login"));

        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeQuery();
        Mockito.verify(resultSet).next();
        Mockito.verify(resultSet).getString(Mockito.anyString());

        Mockito.when(resultSet.next()).thenReturn(false);
        assertNull(securityDao.getPasswordByLogin("login"));

        Mockito.when(preparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> securityDao.getPasswordByLogin("login"));

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> securityDao.getPasswordByLogin("login"));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        securityDao = new JDBCSecurityDao(connection);
        assertDoesNotThrow(() -> securityDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> securityDao.close());
    }
}