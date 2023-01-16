package ua.org.training.library.dto;

import ua.org.training.library.dto.builders.OrderDTOBuilder;

import java.io.Serializable;
import java.util.Objects;

public class OrderDTO implements Serializable {
    private long id;
    private String dateCreated;
    private String dateExpire;
    private BookDTO book;
    private UserDTO user;
    private PlaceDTO place;
    private StatusDTO status;
    private String priceOverdue;
    private boolean chooseDateExpire;

    public OrderDTO(long id, String dateCreated, String dateExpire, BookDTO book, UserDTO user, PlaceDTO place, StatusDTO status, String priceOverdue, boolean chooseDateExpire) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.dateExpire = dateExpire;
        this.book = book;
        this.user = user;
        this.place = place;
        this.status = status;
        this.priceOverdue = priceOverdue;
        this.chooseDateExpire = chooseDateExpire;
    }

    public static OrderDTOBuilder builder() {
        return new OrderDTOBuilder();
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

    public String  getPriceOverdue() {
        return priceOverdue;
    }

    public void setPriceOverdue(String priceOverdue) {
        this.priceOverdue = priceOverdue;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public boolean isChooseDateExpire() {
        return chooseDateExpire;
    }

    public void setChooseDateExpire(boolean chooseDateExpire) {
        this.chooseDateExpire = chooseDateExpire;
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
        if (chooseDateExpire != orderDTO.chooseDateExpire) return false;
        if (!Objects.equals(dateCreated, orderDTO.dateCreated))
            return false;
        if (!Objects.equals(dateExpire, orderDTO.dateExpire)) return false;
        if (!Objects.equals(book, orderDTO.book)) return false;
        if (!Objects.equals(user, orderDTO.user)) return false;
        if (!Objects.equals(place, orderDTO.place)) return false;
        if (!Objects.equals(status, orderDTO.status)) return false;
        return Objects.equals(priceOverdue, orderDTO.priceOverdue);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateExpire != null ? dateExpire.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (priceOverdue != null ? priceOverdue.hashCode() : 0);
        result = 31 * result + (chooseDateExpire ? 1 : 0);
        return result;
    }
}
