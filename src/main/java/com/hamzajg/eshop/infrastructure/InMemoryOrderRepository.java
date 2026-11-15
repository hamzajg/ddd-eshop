package com.hamzajg.eshop.infrastructure;

import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Order> findAllByCustomer(String customerName) {
        return byNumber.values().stream()
                .filter(o -> o.customerName().equals(customerName))
                .collect(Collectors.toList());
    }
}
