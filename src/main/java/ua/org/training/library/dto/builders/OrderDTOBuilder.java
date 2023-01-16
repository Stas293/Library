package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.*;

public class OrderDTOBuilder {
    private long id;
    private String dateCreated;
    private String dateExpire;
    private BookDTO book;
    private UserDTO user;
    private PlaceDTO place;
    private StatusDTO status;
    private String priceOverdue;
    private boolean chooseDateExpire;

    public OrderDTOBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public OrderDTOBuilder setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public OrderDTOBuilder setDateExpire(String dateExpire) {
        this.dateExpire = dateExpire;
        return this;
    }

    public OrderDTOBuilder setBook(BookDTO book) {
        this.book = book;
        return this;
    }

    public OrderDTOBuilder setUser(UserDTO user) {
        this.user = user;
        return this;
    }

    public OrderDTOBuilder setPlace(PlaceDTO place) {
        this.place = place;
        return this;
    }

    public OrderDTOBuilder setStatus(StatusDTO status) {
        this.status = status;
        return this;
    }

    public OrderDTOBuilder setPriceOverdue(String priceOverdue) {
        this.priceOverdue = priceOverdue;
        return this;
    }

    public OrderDTOBuilder setChooseDateExpire(boolean chooseDateExpire) {
        this.chooseDateExpire = chooseDateExpire;
        return this;
    }

    public OrderDTO createOrderDTO() {
        return new OrderDTO(id, dateCreated, dateExpire, book, user, place, status, priceOverdue, chooseDateExpire);
    }
}
