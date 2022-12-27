package ua.org.training.library.utility.page;

import java.util.List;
import java.util.Objects;

public class Page<T> {
    private long pageNumber;
    private long limit;
    private String sorting;
    private String search;
    private long elementsCount;
    private List<T> data;

    public Page() {
    }

    public Page(long pageNumber, long limit, String sorting, String search, long elementsCount, List<T> data) {
        this.pageNumber = pageNumber;
        this.limit = limit;
        this.sorting = sorting;
        this.search = search;
        this.elementsCount = elementsCount;
        this.data = data;
    }

    public static <T> PageBuilder<T> builder() {
        return new PageBuilder<>();
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public long getOffset() {
        return limit * pageNumber;
    }

    public long getElementsCount() {
        return elementsCount;
    }

    public void setElementsCount(long elementsCount) {
        this.elementsCount = elementsCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNumber=" + pageNumber +
                ", size=" + limit +
                ", sorting='" + sorting + '\'' +
                ", search='" + search + '\'' +
                ", totalElements=" + elementsCount +
                ", content=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page<?> page)) return false;

        if (pageNumber != page.pageNumber) return false;
        if (limit != page.limit) return false;
        if (elementsCount != page.elementsCount) return false;
        if (!sorting.equals(page.sorting)) return false;
        if (!Objects.equals(search, page.search)) return false;
        return data.equals(page.data);
    }

    @Override
    public int hashCode() {
        int result = (int) (pageNumber ^ (pageNumber >>> 32));
        result = 31 * result + (int) (limit ^ (limit >>> 32));
        result = 31 * result + sorting.hashCode();
        result = 31 * result + (search != null ? search.hashCode() : 0);
        result = 31 * result + (int) (elementsCount ^ (elementsCount >>> 32));
        result = 31 * result + data.hashCode();
        return result;
    }
}
