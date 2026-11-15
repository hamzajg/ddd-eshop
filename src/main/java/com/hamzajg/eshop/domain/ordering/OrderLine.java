package com.hamzajg.eshop.domain.ordering;

import com.hamzajg.eshop.domain.shared.Money;

import java.util.Objects;

/**
 * One order line: a catalog SKU with the description and unit price
 * snapshotted at order time, so later catalog changes cannot rewrite
 * history. Entity of the {@link Order} aggregate — no identity of its
 * own outside the order.
 */
public final class OrderLine {

    private final String sku;
    private final String description;
    private final Money unitPrice;
    private int quantity;

    public OrderLine(String sku, String description, Money unitPrice, int quantity) {
        this.sku = requireNonBlank(sku, "sku");
        this.description = requireNonBlank(description, "description");
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0, got " + quantity);
        }
        this.quantity = quantity;
    }

    public String sku() {
        return sku;
    }

    public String description() {
        return description;
    }

    public Money unitPrice() {
        return unitPrice;
    }

    public int quantity() {
        return quantity;
    }

    public Money lineTotal() {
        return unitPrice.multiply(quantity);
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
