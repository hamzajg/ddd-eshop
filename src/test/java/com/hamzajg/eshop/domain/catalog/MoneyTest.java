package com.hamzajg.eshop.domain.catalog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void rejectsNegativeAmounts() {
        assertThrows(IllegalArgumentException.class, () -> new Money(-1, "EUR"));
    }

    @Test
    void rejectsBlankCurrency() {
        assertThrows(IllegalArgumentException.class, () -> new Money(100, " "));
        assertThrows(IllegalArgumentException.class, () -> new Money(100, null));
    }

    @Test
    void addsSameCurrency() {
        Money sum = new Money(1999, "EUR").add(new Money(1, "EUR"));
        assertEquals(new Money(2000, "EUR"), sum);
    }

    @Test
    void refusesMixedCurrencies() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(100, "EUR").add(new Money(100, "USD")));
    }

    @Test
    void equalityIsByValue() {
        assertEquals(new Money(500, "USD"), new Money(500, "USD"));
        assertNotEquals(new Money(500, "USD"), new Money(500, "EUR"));
    }
}
