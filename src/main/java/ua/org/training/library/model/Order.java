package ua.org.training.library.model;

import ua.org.training.library.model.builders.OrderBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {
    private Long id;
    private Date dateCreated;
    private Date dateExpire;
    private Book book;
    private User user;
    private Place place;
    private Status status;

    public Order() {
    }

    public Order(Long id, Date dateCreated, Date dateExpire, Book book, User user, Place place) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.dateExpire = dateExpire;
        this.book = book;
        this.user = user;
        this.place = place;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateExpire=" + dateExpire +
                ", book=" + book +
                ", user=" + user +
                ", place=" + place +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;

        if (!Objects.equals(id, order.id)) return false;
        if (!Objects.equals(dateCreated, order.dateCreated)) return false;
        if (!Objects.equals(dateExpire, order.dateExpire)) return false;
        if (!Objects.equals(book, order.book)) return false;
        if (!Objects.equals(user, order.user)) return false;
        if (!Objects.equals(place, order.place)) return false;
        return Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateExpire != null ? dateExpire.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
