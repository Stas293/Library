package ua.org.training.library.model.builders;

import ua.org.training.library.model.Status;

public class StatusBuilder {
    private long id;
    private String code;
    private String name;
    private boolean closed;

    public StatusBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public StatusBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public StatusBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public StatusBuilder setClosed(boolean closed) {
        this.closed = closed;
        return this;
    }

    public Status createStatus() {
        return new Status(id, code, name, closed);
    }
}