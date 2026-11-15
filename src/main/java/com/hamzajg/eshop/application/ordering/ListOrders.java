package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderRepository;

import java.util.List;
import java.util.Objects;

/** Lists the orders belonging to one customer, newest-repo order. */
public final class ListOrders {

    private final OrderRepository orders;

    public ListOrders(OrderRepository orders) {
        this.orders = Objects.requireNonNull(orders, "orders");
    }

    public List<Order> execute(String customerName) {
        Objects.requireNonNull(customerName, "customerName");
        return orders.findAllByCustomer(customerName);
    }
}
