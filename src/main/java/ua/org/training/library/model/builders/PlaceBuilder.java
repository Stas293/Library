package ua.org.training.library.model.builders;

import ua.org.training.library.model.Place;

public class PlaceBuilder {
    private Long id;
    private String name;

    public PlaceBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public PlaceBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public Place createPlace() {
        return new Place(id, name);
    }
}