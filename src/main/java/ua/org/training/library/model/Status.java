package ua.org.training.library.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class Status implements Serializable {
    private long id;
    private String code;
    private String name;
    private boolean closed;
    private Collection<Status> nextStatuses = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Collection<Status> getNextStatuses() {
        return nextStatuses;
    }

    public void setNextStatuses(Collection<Status> nextStatuses) {
        this.nextStatuses = nextStatuses;
    }

    @Override
    public String toString() {
        return "Status{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", nextStatuses=" + nextStatuses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status status)) return false;

        return code.equals(status.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
