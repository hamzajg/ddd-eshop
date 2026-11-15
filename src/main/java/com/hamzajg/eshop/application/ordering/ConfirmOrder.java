package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.ordering.OrderRepository;

/** Confirms a placed order (PLACED → CONFIRMED). */
public final class ConfirmOrder extends OrderTransition {

    public ConfirmOrder(OrderRepository orders) {
        super(orders);
    }

    public String execute(String orderNumber) {
        return apply(orderNumber, order -> order.confirm());
    }
}
