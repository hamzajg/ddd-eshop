package com.hamzajg.eshop.domain.catalog;

import java.util.Objects;

/**
 * Catalog product aggregate root. Identity is the SKU; name and price are
 * mutable through behaviour methods that keep the invariants.
 */
public final class Product {

    private final String sku;
    private String name;
    private Money price;
    private boolean active = true;

    public Product(String sku, String name, Money price) {
        this.sku = requireNonBlank(sku, "sku");
        this.name = requireNonBlank(name, "name");
        this.price = Objects.requireNonNull(price, "price");
    }

    public String sku() {
        return sku;
    }

    public String name() {
        return name;
    }

    public Money price() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public void rename(String newName) {
        this.name = requireNonBlank(newName, "name");
    }

    public void changePrice(Money newPrice) {
        this.price = Objects.requireNonNull(newPrice, "price");
    }

    public void deactivate() {
        this.active = false;
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
