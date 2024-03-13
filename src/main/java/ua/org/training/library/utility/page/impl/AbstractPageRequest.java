package ua.org.training.library.utility.page.impl;


import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.org.training.library.utility.page.Pageable;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public abstract class AbstractPageRequest implements Pageable, Serializable {
    private final int page;
    private final int size;

    public AbstractPageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        } else if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        } else {
            this.page = page;
            this.size = size;
        }
    }

    public int getPageSize() {
        return this.size;
    }

    public int getPageNumber() {
        return this.page;
    }

    public long getOffset() {
        return (long) this.page * (long) this.size;
    }

    public boolean hasPrevious() {
        return this.page > 0;
    }

    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }

    public abstract Pageable previous();
}
