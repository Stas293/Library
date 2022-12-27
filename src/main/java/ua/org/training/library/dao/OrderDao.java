package ua.org.training.library.dao;

import ua.org.training.library.model.Order;
import ua.org.training.library.utility.page.Page;

public interface OrderDao extends GenericDao<Order> {
    Page<Order> getPageByUserId(
            Page<Order> page,
            Long userId);

    Page<Order> getPageByPlaceId(
            Page<Order> page,
            Long placeId);

    Page<Order> getPageByUserIdAndPlaceId(
            Page<Order> page,
            Long userId,
            Long placeId);

    Page<Order> getPageByPlaceName(
            Page<Order> page,
            String placeName);

    Page<Order> getPageByBookId(
            Page<Order> page,
            Long bookId);

    Page<Order> getPageByStatusAndUserId(
            Page<Order> page,
            Long statusId,
            Long userId);

    Page<Order> getPageByStatusId(
            Page<Order> page,
            Long statusId,
            String sortBy);

    Page<Order> getPageByStatusIdAndPlaceId(
            Page<Order> page,
            Long statusId,
            Long placeId,
            String sortBy);
}
