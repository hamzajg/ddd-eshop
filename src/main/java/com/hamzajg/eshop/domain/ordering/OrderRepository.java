package com.hamzajg.eshop.domain.ordering;

import java.util.List;
import java.util.Optional;

/**
 * Order persistence contract. Implementations live in
 * `infrastructure`; the domain only knows this interface.
 */
public interface OrderRepository {

    void save(Order order);

    Optional<Order> findByNumber(String orderNumber);

    /** All orders placed by the given customer name. */
    List<Order> findAllByCustomer(String customerName);
}
