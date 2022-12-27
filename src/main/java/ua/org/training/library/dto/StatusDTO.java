package ua.org.training.library.dto;

import ua.org.training.library.model.Status;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Pair;
import ua.org.training.library.utility.Utility;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StatusDTO implements Serializable {
    private String code;
    private String name;
    private String value;
    private List<Pair<String, String>> nextStatuses;

    public StatusDTO(Locale locale, Status status) {
        this.code = status.getCode();
        this.name = status.getName();
        this.value = Utility.getBundleInterface(locale, Constants.BUNDLE_ORDER_STATUS_PREFIX
                + code.toLowerCase());
        this.nextStatuses = status
                .getNextStatuses()
                .stream()
                .map(nextStatus -> new Pair<>(
                        nextStatus.getCode(),
                        Utility.getBundleInterface(locale,
                                Constants.BUNDLE_ORDER_STATUS_PREFIX
                                        + nextStatus.getCode().toLowerCase()))
                )
                .toList();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List getNextStatuses() {
        return nextStatuses;
    }

    public void setNextStatuses(List<Pair<String, String>> nextStatuses) {
        this.nextStatuses = nextStatuses;
    }

    @Override
    public String toString() {
        return "StatusDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", nextStatuses=" + nextStatuses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusDTO statusDTO)) return false;

        if (!Objects.equals(code, statusDTO.code)) return false;
        if (!Objects.equals(name, statusDTO.name)) return false;
        if (!Objects.equals(value, statusDTO.value)) return false;
        return Objects.equals(nextStatuses, statusDTO.nextStatuses);
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (nextStatuses != null ? nextStatuses.hashCode() : 0);
        return result;
    }
}
