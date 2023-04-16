package ua.org.training.library.utility.page;


import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.Optional;

public interface Pageable {
    static Pageable unpaged() {
        return Unpaged.INSTANCE;
    }

    static Pageable ofSize(int pageSize) {
        return PageRequest.of(0, pageSize);
    }

    default boolean isPaged() {
        return true;
    }

    default boolean isUnpaged() {
        return !this.isPaged();
    }

    int getPageNumber();

    int getPageSize();

    long getOffset();

    Sort getSort();

    default Sort getSortOr(Sort sort) {
        return this.getSort().isSorted() ? this.getSort() : sort;
    }

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    Pageable withPage(int pageNumber);

    boolean hasPrevious();

    default Optional<Pageable> toOptional() {
        return this.isUnpaged() ? Optional.empty() : Optional.of(this);
    }
}

