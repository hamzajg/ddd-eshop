package com.hamzajg.eshop.infrastructure;

import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** In-memory order store for tests and server-free dev runs. */
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> byNumber = new HashMap<>();

    @Override
    public void save(Order order) {
        byNumber.put(order.orderNumber(), order);
    }

    @Override
    public Optional<Order> findByNumber(String orderNumber) {
        return Optional.ofNullable(byNumber.get(orderNumber));
    }
}
