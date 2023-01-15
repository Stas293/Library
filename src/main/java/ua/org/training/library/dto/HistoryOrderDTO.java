package ua.org.training.library.dto;

import ua.org.training.library.dto.builders.HistoryOrderDTOBuilder;
import ua.org.training.library.dto.builders.UserDTOBuilder;
import ua.org.training.library.model.HistoryOrder;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;

public class HistoryOrderDTO implements Serializable {
    private long id;
    private UserDTO user;
    private String bookName;
    private String dateCreated;
    private String dateExpire;
    private StatusDTO status;

    public HistoryOrderDTO(long id, UserDTO user, String bookName, String dateCreated, String dateExpire, StatusDTO status) {
        this.id = id;
        this.user = user;
        this.bookName = bookName;
        this.dateCreated = dateCreated;
        this.dateExpire = dateExpire;
        this.status = status;
    }

    public static HistoryOrderDTOBuilder builder() {
        return new HistoryOrderDTOBuilder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(String dateExpire) {
        this.dateExpire = dateExpire;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HistoryOrderDTO{" +
                "id=" + id +
                ", user=" + user +
                ", bookName='" + bookName + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateExpire='" + dateExpire + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryOrderDTO that)) return false;

        if (id != that.id) return false;
        if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(bookName, that.bookName)) return false;
        if (!Objects.equals(dateCreated, that.dateCreated)) return false;
        if (!Objects.equals(dateExpire, that.dateExpire)) return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (bookName != null ? bookName.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateExpire != null ? dateExpire.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
