package com.hamzajg.eshop.domain.catalog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product() {
        return new Product("SKU-1", "Keyboard", new Money(4999, "EUR"));
    }

    @Test
    void rejectsBlankIdentityAndName() {
        Money price = new Money(100, "EUR");
        assertThrows(IllegalArgumentException.class, () -> new Product(" ", "X", price));
        assertThrows(IllegalArgumentException.class, () -> new Product("S", null, price));
        assertThrows(IllegalArgumentException.class, () -> new Product("S", " ", price));
    }

    @Test
    void rejectsNullPrice() {
        assertThrows(NullPointerException.class, () -> new Product("S", "X", null));
    }

    @Test
    void startsActive() {
        assertTrue(product().isActive());
    }

    @Test
    void renameAndRepriceKeepInvariants() {
        Product p = product();
        p.rename("Mechanical Keyboard");
        p.changePrice(new Money(5999, "EUR"));

        assertEquals("Mechanical Keyboard", p.name());
        assertEquals(new Money(5999, "EUR"), p.price());
        assertThrows(IllegalArgumentException.class, () -> p.rename(""));
    }

    @Test
    void deactivateIsTerminalForPurchasing() {
        Product p = product();
        p.deactivate();
        assertFalse(p.isActive());
    }
}
