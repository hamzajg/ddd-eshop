package com.hamzajg.eshop.domain.customer;

/**
 * Customer aggregate. Identity is the shopper's name for the MVP —
 * credentials are out of scope (see specs/shopper-accounts-order-history).
 */
public final class Customer {

    private final String name;

    public Customer(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("customer name must not be blank");
        }
        this.name = name.trim();
    }

    public String name() {
        return name;
    }
}
