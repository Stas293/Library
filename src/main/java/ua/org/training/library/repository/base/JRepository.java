package ua.org.training.library.repository.base;

public interface JRepository<T, ID> extends CrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID> {
}