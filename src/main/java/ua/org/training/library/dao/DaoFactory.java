package ua.org.training.library.dao;

import ua.org.training.library.dao.impl.JDBCDaoFactory;
import ua.org.training.library.exceptions.JDBCException;

public abstract class DaoFactory {
    private static volatile DaoFactory daoFactory;

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    daoFactory = new JDBCDaoFactory();
                }
            }
        }
        return daoFactory;
    }

    public abstract AuthorDao createAuthorDao() throws JDBCException;

    public abstract BookDao createBookDao() throws JDBCException;

    public abstract HistoryOrderDao createHistoryOrderDao() throws JDBCException;

    public abstract OrderDao createOrderDao() throws JDBCException;

    public abstract PlaceDao createPlaceDao() throws JDBCException;

    public abstract RoleDao createRoleDao() throws JDBCException;

    public abstract UserDao createUserDao() throws JDBCException;

    public abstract SecurityDao createSecurityDao() throws JDBCException;

    public abstract StatusDao createStatusDao() throws JDBCException;
}
