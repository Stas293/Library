package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.HistoryOrderDTO;
import ua.org.training.library.dto.StatusDTO;
import ua.org.training.library.dto.UserDTO;

public class HistoryOrderDTOBuilder {
    private long id;
    private UserDTO user;
    private String bookName;
    private String dateCreated;
    private String dateExpire;
    private StatusDTO status;

    public HistoryOrderDTOBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public HistoryOrderDTOBuilder setUser(UserDTO user) {
        this.user = user;
        return this;
    }

    public HistoryOrderDTOBuilder setBookName(String bookName) {
        this.bookName = bookName;
        return this;
    }

    public HistoryOrderDTOBuilder setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public HistoryOrderDTOBuilder setDateExpire(String dateExpire) {
        this.dateExpire = dateExpire;
        return this;
    }

    public HistoryOrderDTOBuilder setStatus(StatusDTO status) {
        this.status = status;
        return this;
    }

    public HistoryOrderDTO createHistoryOrderDTO() {
        return new HistoryOrderDTO(id, user, bookName, dateCreated, dateExpire, status);
    }
}
