package com.hamzajg.eshop.domain.ordering;

/**
 * Order lifecycle. PLACED → CONFIRMED → SHIPPED; CANCELLED reachable from
 * PLACED or CONFIRMED only. Terminal states are SHIPPED and CANCELLED.
 */
public enum OrderStatus {
    PLACED,
    CONFIRMED,
    SHIPPED,
    CANCELLED;

    public boolean isTerminal() {
        return this == SHIPPED || this == CANCELLED;
    }
}
