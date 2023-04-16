package ua.org.training.library.utility.page.impl;

public class PageRequest extends AbstractPageRequest {
    private final Sort sort;

    protected PageRequest(int page, int size, Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    public static PageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    public static PageRequest of(int page, int size, Sort.Direction direction, String... properties) {
        return of(page, size, Sort.by(direction, properties));
    }

    public static PageRequest ofSize(int pageSize) {
        return of(0, pageSize);
    }

    public Sort getSort() {
        return this.sort;
    }

    public PageRequest next() {
        return new PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    public PageRequest previous() {
        return this.getPageNumber() == 0 ? this : new PageRequest(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }

    public PageRequest first() {
        return new PageRequest(0, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if ((obj instanceof PageRequest that)) {
            return super.equals(that) && this.sort.equals(that.sort);
        }
        return false;
    }

    public PageRequest withPage(int pageNumber) {
        return new PageRequest(pageNumber, this.getPageSize(), this.getSort());
    }

    public PageRequest withSort(Sort.Direction direction, String... properties) {
        return new PageRequest(this.getPageNumber(), this.getPageSize(), Sort.by(direction, properties));
    }

    public PageRequest withSort(Sort sort) {
        return new PageRequest(this.getPageNumber(), this.getPageSize(), sort);
    }

    public int hashCode() {
        return 31 * super.hashCode() + this.sort.hashCode();
    }

    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", this.getPageNumber(), this.getPageSize(), this.sort);
    }
}

