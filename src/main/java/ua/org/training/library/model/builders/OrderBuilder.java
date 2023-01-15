package ua.org.training.library.model.builders;

import ua.org.training.library.model.*;

import java.util.Date;

public class OrderBuilder {
    private Long id;
    private Date dateCreated;
    private Date dateExpire;
    private Book book;
    private User user;
    private Place place;
    private Status status;

    public OrderBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public OrderBuilder setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
        return this;
    }

    public OrderBuilder setBook(Book book) {
        this.book = book;
        return this;
    }

    public OrderBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public OrderBuilder setPlace(Place place) {
        this.place = place;
        return this;
    }

    public OrderBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Order createOrder() {
        return new Order(id, dateCreated, dateExpire, book, user, place, status);
    }
}