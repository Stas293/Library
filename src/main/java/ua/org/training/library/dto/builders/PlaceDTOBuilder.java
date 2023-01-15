package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.PlaceDTO;

public class PlaceDTOBuilder {
    private String name;
    private String data;

    public PlaceDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlaceDTOBuilder setData(String data) {
        this.data = data;
        return this;
    }

    public PlaceDTO createPlaceDTO() {
        return new PlaceDTO(name, data);
    }
}
