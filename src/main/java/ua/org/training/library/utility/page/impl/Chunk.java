package ua.org.training.library.utility.page.impl;


import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.Slice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@EqualsAndHashCode
@ToString
abstract class Chunk<T> implements Slice<T>, Serializable {
    private final List<T> content = new ArrayList<>();
    private final Pageable pageable;

    public Chunk(List<T> content, Pageable pageable) {
        this.content.addAll(content);
        this.pageable = pageable;
    }

    public int getNumber() {
        return this.pageable.isPaged() ? this.pageable.getPageNumber() : 0;
    }

    public int getSize() {
        return this.pageable.isPaged() ? this.pageable.getPageSize() : this.content.size();
    }

    public int getNumberOfElements() {
        return this.content.size();
    }

    public boolean hasPrevious() {
        return this.getNumber() > 0;
    }

    public boolean isFirst() {
        return !this.hasPrevious();
    }

    public boolean isLast() {
        return !this.hasNext();
    }

    public boolean hasContent() {
        return !this.content.isEmpty();
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    public Pageable getPageable() {
        return this.pageable;
    }

    public Sort getSort() {
        return this.pageable.getSort();
    }

    public Iterator<T> iterator() {
        return this.content.iterator();
    }
}

