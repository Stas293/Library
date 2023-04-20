package ua.org.training.library.utility.page.impl;


import lombok.EqualsAndHashCode;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class PageImpl<T> extends Chunk<T> implements Page<T> {
    private final long total;

    public PageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable);
        this.total = pageable.toOptional()
                .filter(it -> !content.isEmpty())
                .filter(it -> it.getOffset() + it.getPageSize() > total)
                .map(it -> it.getOffset() + content.size())
                .orElse(total);
    }

    public PageImpl(List<T> content) {
        this(content, Pageable.unpaged(), null == content ? 0L : (long)content.size());
    }

    public int getTotalPages() {
        return this.getSize() == 0 ? 1 : (int)Math.ceil((double)this.total / (double)this.getSize());
    }

    public long getTotalElements() {
        return this.total;
    }

    public boolean hasNext() {
        return this.getNumber() + 1 < this.getTotalPages();
    }
    public boolean isLast() {
        return !this.hasNext();
    }

    @Override
    public String toString() {
        return "PageImpl{" +
                "total=" + total +
                super.toString() +
                '}';
    }
}

