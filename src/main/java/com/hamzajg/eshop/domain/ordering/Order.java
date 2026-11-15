package com.hamzajg.eshop.domain.ordering;

import com.hamzajg.eshop.domain.shared.Money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Order aggregate root. Identity is the order number; lines are entities
 * owned by the order. All lines must share one currency — the order total
 * would otherwise be meaningless.
 *
 * Lifecycle: created via {@link #place}, mutable only while PLACED,
 * transitions enforced by {@link #confirm}, {@link #ship}, {@link #cancel}.
 */
public final class Order {

    private final String orderNumber;
    private final String customerName;
    private final List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status = OrderStatus.PLACED;

    private Order(String orderNumber, String customerName) {
        this.orderNumber = requireNonBlank(orderNumber, "orderNumber");
        this.customerName = requireNonBlank(customerName, "customerName");
    }

    /** Places a new order for a customer. Requires at least one line. */
    public static Order place(String orderNumber, String customerName, List<OrderLine> lines) {
        Order order = new Order(orderNumber, customerName);
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("an order needs at least one line");
        }
        lines.forEach(order::addLineInternal);
        return order;
    }

    public String orderNumber() {
        return orderNumber;
    }

    /** Name of the shopper who placed the order (snapshot at placement). */
    public String customerName() {
        return customerName;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderLine> lines() {
        return Collections.unmodifiableList(lines);
    }

    /** Sum of all line totals. */
    public Money total() {
        Money sum = null;
        for (OrderLine line : lines) {
            Money lineTotal = line.lineTotal();
            sum = sum == null ? lineTotal : sum.add(lineTotal);
        }
        return Objects.requireNonNull(sum, "order has no lines");
    }

    public void confirm() {
        transitionTo(OrderStatus.CONFIRMED, OrderStatus.PLACED);
    }

    public void ship() {
        transitionTo(OrderStatus.SHIPPED, OrderStatus.CONFIRMED);
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("cannot cancel an order in status " + status);
        }
        status = OrderStatus.CANCELLED;
    }

    private void transitionTo(OrderStatus target, OrderStatus expected) {
        if (status != expected) {
            throw new IllegalStateException(
                    "cannot move order from " + status + " to " + target);
        }
        status = target;
    }

    private void addLineInternal(OrderLine line) {
        Objects.requireNonNull(line, "line");
        if (!lines.isEmpty()
                && !lines.get(0).unitPrice().currency().equals(line.unitPrice().currency())) {
            throw new IllegalArgumentException(
                    "all lines must share the order currency "
                            + lines.get(0).unitPrice().currency());
        }
        lines.add(line);
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
