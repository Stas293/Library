package ua.org.training.library.utility.page.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

@EqualsAndHashCode
@ToString
public class Sort implements Serializable {
    private static final Sort UNSORTED = unsorted();
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;
    private final List<Order> orders;

    protected Sort(List<Order> orders) {
        this.orders = orders;
    }

    public static Sort by(List<Order> orders) {
        return orders.isEmpty() ? unsorted() : new Sort(orders);
    }

    private Sort(Direction direction, List<String> properties) {
        if (properties != null && !properties.isEmpty()) {
            this.orders = properties.stream().map((it) -> new Order(direction, it)).toList();
        } else {
            throw new IllegalArgumentException("You have to provide at least one property to sort by");
        }
    }

    public static Sort by(String... properties) {
        return properties.length == 0 ? unsorted() : new Sort(DEFAULT_DIRECTION, Arrays.asList(properties));
    }

    public static Sort by(Direction direction, String... properties) {
        return by(Arrays.stream(properties).map((it) -> new Order(direction, it)).toList());
    }

    public static Sort by(Order... orders) {
        return new Sort(Arrays.asList(orders));
    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public boolean isSorted() {
        return !this.isEmpty();
    }

    public boolean isEmpty() {
        return this.orders.isEmpty();
    }

    public boolean isUnsorted() {
        return !this.isSorted();
    }

    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }
    public Order getOrderFor(String property) {

        for (Order order : this.orders) {
            if (order.getProperty().equals(property)) {
                return order;
            }
        }

        return null;
    }

    public boolean isOrdered() {
        return !this.orders.isEmpty();
    }

    @ToString
    public enum Direction {
        ASC,
        DESC;

        Direction() {
        }

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }

        public static Direction fromString(String value) {
            try {
                return valueOf(value.toUpperCase(Locale.US));
            } catch (Exception var2) {
                throw new IllegalArgumentException(String.format("Invalid value '%s' for orders given; Has to be either 'desc' or 'asc' (case insensitive)", value), var2);
            }
        }

        public static Optional<Direction> fromOptionalString(String value) {
            try {
                return Optional.of(fromString(value));
            } catch (IllegalArgumentException var2) {
                return Optional.empty();
            }
        }
    }

    @EqualsAndHashCode
    @ToString
    public static class Order implements Serializable {
        private final Direction direction;
        private final String property;
        private final boolean ignoreCase;

        public Order(Direction direction, String property) {
            this(direction, property, false);
        }

        public static Order by(String property) {
            return new Order(Sort.DEFAULT_DIRECTION, property);
        }

        public static Order asc(String property) {
            return new Order(Direction.ASC, property);
        }

        public static Order desc(String property) {
            return new Order(Direction.DESC, property);
        }

        private Order(Direction direction, String property, boolean ignoreCase) {
            this.direction = direction == null ? Sort.DEFAULT_DIRECTION : direction;
            this.property = property;
            this.ignoreCase = ignoreCase;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public String getProperty() {
            return this.property;
        }

        public boolean isAscending() {
            return this.direction.isAscending();
        }

        public boolean isDescending() {
            return this.direction.isDescending();
        }

        public boolean isIgnoreCase() {
            return this.ignoreCase;
        }

        public Order with(Direction direction) {
            return new Order(direction, this.property, this.ignoreCase);
        }

        public Order withProperty(String property) {
            return new Order(this.direction, property, this.ignoreCase);
        }

        public Sort withProperties(String... properties) {
            return Sort.by(this.direction, properties);
        }

        public Order ignoreCase() {
            return new Order(this.direction, this.property, true);
        }
    }
}
