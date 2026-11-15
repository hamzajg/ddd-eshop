package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.ordering.OrderRepository;

/** Ships a confirmed order (CONFIRMED → SHIPPED). */
public final class ShipOrder extends OrderTransition {

    public ShipOrder(OrderRepository orders) {
        super(orders);
    }

    public String execute(String orderNumber) {
        return apply(orderNumber, order -> order.ship());
    }
}
