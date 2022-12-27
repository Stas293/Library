package ua.org.training.library.model;

import ua.org.training.library.model.builders.HistoryOrderBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class HistoryOrder implements Serializable {
    private Long id;
    private User user;
    private String bookName;
    private Date dateCreated;
    private Date dateExpire;
    private Status status;

    public HistoryOrder() {
    }

    public HistoryOrder(Long id, User user, String bookName, Date dateCreated, Date dateExpire, Status status) {
        this.id = id;
        this.user = user;
        this.bookName = bookName;
        this.dateCreated = dateCreated;
        this.dateExpire = dateExpire;
        this.status = status;
    }

    public static HistoryOrderBuilder builder() {
        return new HistoryOrderBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HistoryOrder{" +
                "id=" + id +
                ", user=" + user +
                ", bookName='" + bookName + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateExpire=" + dateExpire +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryOrder that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(bookName, that.bookName)) return false;
        if (!Objects.equals(dateCreated, that.dateCreated)) return false;
        if (!Objects.equals(dateExpire, that.dateExpire)) return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (bookName != null ? bookName.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateExpire != null ? dateExpire.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
