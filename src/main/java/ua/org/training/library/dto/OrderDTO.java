package ua.org.training.library.dto;

import ua.org.training.library.model.Order;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class OrderDTO implements Serializable {
    private long id;
    private String dateCreated;
    private String dateExpire;
    private BookDTO book;
    private UserDTO user;
    private PlaceDTO place;
    private StatusDTO status;
    private double priceOverdue;

    public OrderDTO(Locale locale, Order order) {
        this.id = order.getId();
        this.dateCreated = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        if (order.getDateExpire() != null) this.dateExpire = order.getDateExpire().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        else this.dateExpire = "Not set";
        this.book = new BookDTO(locale, order.getBook());
        this.user = new UserDTO(order.getUser());
        this.place = new PlaceDTO(locale, order.getPlace());
        this.status = new StatusDTO(locale, order.getStatus());
        if (order.getDateExpire() != null) this.priceOverdue = order.getBook().getFine() *
                (new Date().toInstant().atOffset(ZoneOffset.UTC).toLocalDate().toEpochDay() -
                        order.getDateExpire().toInstant().atOffset(ZoneOffset.UTC).toLocalDate().toEpochDay());
        else this.priceOverdue = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
    }

    public double getPriceOverdue() {
        return priceOverdue;
    }

    public void setPriceOverdue(double priceOverdue) {
        this.priceOverdue = priceOverdue;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateExpire='" + dateExpire + '\'' +
                ", book=" + book +
                ", user=" + user +
                ", place=" + place +
                ", status=" + status +
                ", priceOverdue=" + priceOverdue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDTO orderDTO)) return false;

        if (id != orderDTO.id) return false;
        if (Double.compare(orderDTO.priceOverdue, priceOverdue) != 0) return false;
        if (!Objects.equals(dateCreated, orderDTO.dateCreated))
            return false;
        if (!Objects.equals(dateExpire, orderDTO.dateExpire)) return false;
        if (!Objects.equals(book, orderDTO.book)) return false;
        if (!Objects.equals(user, orderDTO.user)) return false;
        if (!Objects.equals(place, orderDTO.place)) return false;
        return Objects.equals(status, orderDTO.status);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateExpire != null ? dateExpire.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        temp = Double.doubleToLongBits(priceOverdue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
