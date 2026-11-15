package com.hamzajg.eshop.domain.ordering;

import com.hamzajg.eshop.domain.shared.Money;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private static OrderLine line(String sku, long priceMinor, int qty) {
        return new OrderLine(sku, "desc-" + sku, new Money(priceMinor, "EUR"), qty);
    }

    private static Order twoLineOrder() {
        return Order.place("ORD-1", "alice", List.of(
                line("SKU-A", 1000, 2),
                line("SKU-B", 500, 1)));
    }

    @Test
    void customerNameIsRecorded() {
        Order order = twoLineOrder();
        assertEquals("alice", order.customerName());
    }

    @Test
    void blankCustomerNameRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.place("ORD", " ", List.of(line("S", 1, 1))));
    }

    @Test
    void placeRequiresOrderNumberAndLines() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.place(" ", "alice", List.of(line("S", 1, 1))));
        assertThrows(IllegalArgumentException.class,
                () -> Order.place("ORD", "alice", List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> Order.place("ORD", "alice", null));
        assertThrows(NullPointerException.class,
                () -> Order.place("ORD", "alice", Arrays.asList((OrderLine) null)));
    }

    @Test
    void startsPlacedWithSnapshotLines() {
        Order order = twoLineOrder();
        assertEquals("ORD-1", order.orderNumber());
        assertEquals(OrderStatus.PLACED, order.status());
        assertEquals(2, order.lines().size());
        assertThrows(UnsupportedOperationException.class,
                () -> order.lines().add(line("X", 1, 1)));
    }

    @Test
    void totalSumsLineTotals() {
        Order order = twoLineOrder();
        // 2 * 1000 + 1 * 500 = 2500 minor units
        assertEquals(new Money(2500, "EUR"), order.total());
    }

    @Test
    void rejectsMixedCurrenciesAcrossLines() {
        OrderLine eur = line("SKU-A", 1000, 1);
        OrderLine usd = new OrderLine("SKU-B", "desc", new Money(500, "USD"), 1);
        assertThrows(IllegalArgumentException.class,
                () -> Order.place("ORD", "alice", List.of(eur, usd)));
    }

    @Test
    void lifecycleFollowsPlacedConfirmedShipped() {
        Order order = twoLineOrder();
        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.status());
        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.status());
        assertTrue(order.status().isTerminal());
    }

    @Test
    void invalidTransitionsAreRejected() {
        Order order = twoLineOrder();
        // ship before confirm
        assertThrows(IllegalStateException.class, order::ship);
        // confirm twice
        order.confirm();
        assertThrows(IllegalStateException.class, order::confirm);
        // cancel after ship
        order.ship();
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void cancelAllowedBeforeShipping() {
        Order order = twoLineOrder();
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.status());
        assertThrows(IllegalStateException.class, order::confirm);
    }
}
