package com.hamzajg.eshop.domain.ordering;

import java.util.Optional;

/**
 * Order persistence contract. Implementations live in
 * `infrastructure`; the domain only knows this interface.
 */
public interface OrderRepository {

    void save(Order order);

    Optional<Order> findByNumber(String orderNumber);
}
