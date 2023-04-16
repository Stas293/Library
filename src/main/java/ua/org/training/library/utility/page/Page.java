package ua.org.training.library.utility.page;


import ua.org.training.library.utility.page.impl.PageImpl;

import java.util.Collections;

public interface Page<T> extends Slice<T> {
    static <T> Page<T> empty(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0L);
    }

    int getTotalPages();

    long getTotalElements();
}
