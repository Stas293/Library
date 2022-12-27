package ua.org.training.library.model.builders;

import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.Status;
import ua.org.training.library.model.User;

import java.util.Date;

public class HistoryOrderBuilder {
    private Long id;
    private User user;
    private String bookName;
    private Date dateCreated;
    private Date dateExpire;
    private Status status;

    public HistoryOrderBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public HistoryOrderBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public HistoryOrderBuilder setBookName(String bookName) {
        this.bookName = bookName;
        return this;
    }

    public HistoryOrderBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public HistoryOrderBuilder setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
        return this;
    }

    public HistoryOrderBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public HistoryOrder createHistoryOrder() {
        return new HistoryOrder(id, user, bookName, dateCreated, dateExpire, status);
    }
}