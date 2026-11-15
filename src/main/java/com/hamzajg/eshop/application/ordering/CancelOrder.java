package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.ordering.OrderRepository;

/** Cancels an order that has not shipped (PLACED/CONFIRMED → CANCELLED). */
public final class CancelOrder extends OrderTransition {

    public CancelOrder(OrderRepository orders) {
        super(orders);
    }

    public String execute(String orderNumber) {
        return apply(orderNumber, order -> order.cancel());
    }
}
