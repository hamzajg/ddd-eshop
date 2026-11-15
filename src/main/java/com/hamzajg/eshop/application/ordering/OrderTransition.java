package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderRepository;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Shared skeleton for single-transition order use cases: load the order,
 * apply the domain transition, persist. Domain invariants
 * ({@code IllegalStateException} on bad transitions, missing order) are
 * propagated unchanged.
 */
abstract class OrderTransition {

    private final OrderRepository orders;

    OrderTransition(OrderRepository orders) {
        this.orders = Objects.requireNonNull(orders, "orders");
    }

    /** Applies {@code transition} to the order and returns its new status name. */
    protected String apply(String orderNumber, Consumer<Order> transition) {
        Order order = orders.findByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "unknown order: " + orderNumber));
        transition.accept(order);
        orders.save(order);
        return order.status().name();
    }
}
