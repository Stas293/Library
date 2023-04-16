package ua.org.training.library.utility.page;

import com.project.university.system_library.utility.page.impl.PageRequest;
import com.project.university.system_library.utility.page.impl.Sort;

import java.util.List;

public interface Slice<T> {
    int getNumber();

    int getSize();

    int getNumberOfElements();

    List<T> getContent();

    boolean hasContent();

    Sort getSort();

    boolean isFirst();

    boolean isLast();

    boolean hasNext();

    boolean hasPrevious();

    default Pageable getPageable() {
        return PageRequest.of(this.getNumber(), this.getSize(), this.getSort());
    }
}
