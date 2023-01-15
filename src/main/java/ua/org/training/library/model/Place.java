package ua.org.training.library.model;

import ua.org.training.library.model.builders.PlaceBuilder;

import java.io.Serializable;

public class Place implements Serializable {
    private Long id;
    private String name;

    public Place() {
    }

    public Place(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PlaceBuilder builder() {
        return new PlaceBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;

        Place place = (Place) o;

        if (!id.equals(place.id)) return false;
        return name.equals(place.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
