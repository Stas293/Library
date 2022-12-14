package ua.org.training.library.dto;

import ua.org.training.library.model.Place;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;

import java.io.Serializable;
import java.util.Locale;

public class PlaceDTO implements Serializable {
    private String name;
    private String data;

    public PlaceDTO(Locale locale, Place place) {
        this.name = place.getName();
        this.data = Utility.getBundleInterface(
                locale,
                Constants.BUNDLE_PLACE_PREFIX + name.toLowerCase().replace(" ", "."));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PlaceDTO{" +
                "name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceDTO placeDTO)) return false;

        if (!name.equals(placeDTO.name)) return false;
        return data.equals(placeDTO.data);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }
}
