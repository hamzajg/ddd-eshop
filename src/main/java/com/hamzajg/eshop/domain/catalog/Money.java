package com.hamzajg.eshop.domain.catalog;

/**
 * Money value object: an amount in minor units (cents) plus an ISO-4217
 * currency code. Immutable; arithmetic returns new instances and refuses
 * to mix currencies.
 */
public record Money(long amountMinor, String currency) {

    public Money {
        if (amountMinor < 0) {
            throw new IllegalArgumentException("amount must be >= 0, got " + amountMinor);
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency is required");
        }
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "cannot add " + other.currency + " to " + currency);
        }
        return new Money(amountMinor + other.amountMinor, currency);
    }
}
