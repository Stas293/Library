package ua.org.training.library.utility.page;

import java.util.List;

public class PageBuilder<T> {
    private long pageNumber;
    private long limit;
    private String sorting;
    private String search;
    private long elementsCount;
    private List<T> data;

    public PageBuilder<T> setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public PageBuilder<T> setLimit(long limit) {
        this.limit = limit;
        return this;
    }

    public PageBuilder<T> setSorting(String sorting) {
        this.sorting = sorting;
        return this;
    }

    public PageBuilder<T> setSearch(String search) {
        this.search = search;
        return this;
    }

    public PageBuilder<T> setElementsCount(long elementsCount) {
        this.elementsCount = elementsCount;
        return this;
    }

    public PageBuilder<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public Page<T> createPage() {
        return new Page<>(pageNumber, limit, sorting, search, elementsCount, data);
    }
}