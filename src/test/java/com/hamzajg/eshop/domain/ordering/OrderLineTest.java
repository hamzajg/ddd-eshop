package com.hamzajg.eshop.domain.ordering;

import com.hamzajg.eshop.domain.shared.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineTest {

    @Test
    void rejectsBlankSkuDescriptionAndBadQuantity() {
        Money price = new Money(100, "EUR");
        assertThrows(IllegalArgumentException.class,
                () -> new OrderLine(" ", "d", price, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new OrderLine("S", null, price, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new OrderLine("S", "d", price, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new OrderLine("S", "d", price, -3));
        assertThrows(NullPointerException.class,
                () -> new OrderLine("S", "d", null, 1));
    }

    @Test
    void lineTotalMultipliesUnitPriceByQuantity() {
        OrderLine line = new OrderLine("SKU", "desc", new Money(999, "EUR"), 3);
        assertEquals(new Money(2997, "EUR"), line.lineTotal());
    }
}
